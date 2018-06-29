package com.cheesygames.colonysimulation.math.bounding.octree;

import com.cheesygames.colonysimulation.math.bounding.AABB;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.jme3.math.Vector3f;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Semi-dynamic octree, which allows the user to add, remove and find entities. It does not support [yet] the update of entity positions. That's there's the word "semi" in
 * semi-dynamic.
 *
 * @param <E> Entity Type.
 */
public class Octree<E extends IOctreeEntity> {

    private Map<Vector3i, OctreeNode<E>> m_nodes;

    public Octree() {
        this.m_nodes = new HashMap<>();
    }

    /**
     * Gets the position's super index. Its dimension doesn't matter because octree nodes are cubes. A super index is the index of a biggest node.
     *
     * @param position The position to compute its super index.
     *
     * @return The position's super index.
     */
    public static int getSuperIndex(float position) {
        return ((int) Math.floor(position)) >> OctreeNode.MAX_DEPTH;
    }

    /**
     * Gets the position's super index. Its dimension doesn't matter because octree nodes are cubes. A super index is the index of a biggest node.
     *
     * @param position The position to compute its super index.
     *
     * @return The position's super index.
     */
    public static Vector3i getSuperIndex(Vector3f position) {
        return new Vector3i(getSuperIndex(position.x), getSuperIndex(position.y), getSuperIndex(position.z));
    }

    /**
     * Gets the position's index according to the supplied depth. Its dimension doesn't matter because octree nodes are cubes.
     *
     * @param position The position to compute its index.
     * @param depth    The depth of the node from which to compute the index.
     *
     * @return The position's index.
     */
    public static int getIndex(float position, int depth) {
        return ((int) Math.floor(position)) >> depth;
    }

    /**
     * Checks if the AABB fits in the supplied relative depth. It only works if it is known the AABB fits in the same super node.
     *
     * @param aabb  The AABB to check.
     * @param depth The depth to check whether the AABB fits in it.
     *
     * @return True if the supplied AABB fits in the given depth, false otherwise.
     */
    public static boolean doesAABBFitInRelativeDepth(AABB aabb, int depth) {
        // Absolute indices, there's no need to check with relative indices.
        boolean indexX = Octree.getIndex(aabb.getMin().x, depth) == Octree.getIndex(aabb.getMax().x, depth);
        boolean indexY = Octree.getIndex(aabb.getMin().y, depth) == Octree.getIndex(aabb.getMax().y, depth);
        boolean indexZ = Octree.getIndex(aabb.getMin().z, depth) == Octree.getIndex(aabb.getMax().z, depth);

        return indexX && indexY && indexZ;
    }

    /**
     * Checks if the AABB fits in the supplied absolute depth and index.
     *
     * @param aabb  The AABB to check.
     * @param index The super index where we want to know if the AABB fits.
     * @param depth The depth to check whether the AABB fits in it.
     *
     * @return True if the supplied AABB fits in the given depth, false otherwise.
     */
    public static boolean doesAABBFitInDepth(AABB aabb, Vector3i index, int depth) {
        // Absolute indices, there's no need to check with relative indices.
        boolean indexX = Octree.getIndex(aabb.getMin().x, depth) == Octree.getIndex(aabb.getMax().x, depth);
        boolean indexY = Octree.getIndex(aabb.getMin().y, depth) == Octree.getIndex(aabb.getMax().y, depth);
        boolean indexZ = Octree.getIndex(aabb.getMin().z, depth) == Octree.getIndex(aabb.getMax().z, depth);

        boolean superIndexX = getSuperIndex(aabb.getMin().x) == index.x && getSuperIndex(aabb.getMax().x) == index.x;
        boolean superIndexY = getSuperIndex(aabb.getMin().y) == index.y && getSuperIndex(aabb.getMax().y) == index.y;
        boolean superIndexZ = getSuperIndex(aabb.getMin().z) == index.z && getSuperIndex(aabb.getMax().z) == index.z;

        return indexX && indexY && indexZ && superIndexX && superIndexY && superIndexZ;
    }

    /**
     * Finds all the nodes that are directly holding a reference to the entity sought for.
     *
     * @param soughtEntity The entity that is sought for.
     *
     * @return All the nodes that are directly holding a reference to the entity sought for.
     */
    public List<OctreeNode<E>> findEntityNodes(E soughtEntity) {
        List<OctreeNode<E>> soughtEntityHolders = new ArrayList<>();

        traverseIntersectingNodesEntitiesRecursive(soughtEntity.getAABB(), (node, entity) -> {
            if (entity.equals(soughtEntity)) {
                soughtEntityHolders.add(node);
            }
        });

        return soughtEntityHolders;
    }

    /**
     * Traverses all entities in the node at the supplied index and in all its children nodes. It is recursive.
     *
     * @param index               The index at which the node we want to traverse is.
     * @param operationOnEntities The operation to execute on the queried entities.
     */
    public void traverseNodeEntitiesRecursive(Vector3i index, BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        traverseNodeRecursive(index, (node) -> {
            node.m_entities.forEach((entity) -> {
                operationOnEntities.accept(node, entity);
            });
        });
    }

    /**
     * Traverses all nodes at with the maximum depth an apply the operation on them. It is not recursive.
     *
     * @param operationOnNodes The operation to execute on each node.
     */
    public void traverseAllNodes(Consumer<OctreeNode<E>> operationOnNodes) {
        Set<Vector3i> indices = m_nodes.keySet();
        for (Vector3i index : indices) {
            operationOnNodes.accept(m_nodes.get(index));
        }
    }

    /**
     * Traverses all nodes and their children and apply the operation on them. It is recursive.
     *
     * @param operationOnNodes The operation to execute on each node.
     */
    public void traverseAllNodesRecursive(Consumer<OctreeNode<E>> operationOnNodes) {
        Set<Vector3i> indices = m_nodes.keySet();
        for (Vector3i index : indices) {
            traverseNodeRecursive(index, operationOnNodes);
        }
    }

    /**
     * Traverse all nodes and their children and for each node, apply the operation on their entities. It is recursive. Entities that are shared across multiple super nodes will be
     * traversed multiple times.
     *
     * @param operationOnEntities The operation to execute on all entities.
     */
    public void traverseAllNodesEntitiesRecursive(BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        Set<Vector3i> indices = m_nodes.keySet();
        for (Vector3i index : indices) {
            traverseNodeEntitiesRecursive(index, operationOnEntities);
        }
    }

    /**
     * Traverse all nodes and their children and for each node, apply the operation on their entities distinctively. A collection is used to not repeat the execution of the
     * supplied operation on an entity twice. It is recursive. Entities that are shared across multiple super nodes will be traversed multiple times.
     *
     * @param operationOnEntities The operation to execute on all entities.
     */
    public void traverseAllNodesEntitiesRecursiveDistinct(BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        Set<E> traversedEntities = new HashSet<>();
        traverseAllNodesEntitiesRecursive((node, navMesh) -> {
            if (!traversedEntities.contains(navMesh)) {
                operationOnEntities.accept(node, navMesh);
                traversedEntities.add(navMesh);
            }
        });
    }

    /**
     * Applies the operation on the node at the supplied index and then recursively traverse all sub nodes and do the same.
     *
     * @param index            The index at which the node we want to traverse is.
     * @param operationOnNodes The operation to execute on all nodes.
     */
    public void traverseNodeRecursive(Vector3i index, Consumer<OctreeNode<E>> operationOnNodes) {
        OctreeNode<E> node = m_nodes.get(index);

        if (node != null) {
            node.traverseNodeRecursive(operationOnNodes);
        }
    }

    /**
     * Traverses all entities in all nodes colliding to the supplied AABB and their sub-nodes and apply the given operation. It is recursive.
     *
     * @param aabb                The AABB in which to pick the nodes to traverse.
     * @param operationOnEntities The operation to execute on the queried entities.
     */
    public void traverseNodesEntitiesRecursive(AABB aabb, BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        traverseNodes(aabb, (node) -> {
            node.traverseNodeEntitiesRecursive(operationOnEntities);
        });
    }

    /**
     * Traverses all entities in all nodes colliding to the supplied AABB and their sub-nodes and apply the given operation. It is recursive.
     *
     * @param aabb                The AABB in which to pick the nodes to traverse.
     * @param minDepth            The inclusive minimum depth. Searching for entities in lower depths than this one is unnecessary.
     * @param operationOnEntities The operation to execute on the queried entities.
     */
    public void traverseNodesEntitiesRecursive(AABB aabb, int minDepth, BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        traverseNodes(aabb, (node) -> {
            node.traverseNodeEntitiesRecursive(minDepth, operationOnEntities);
        });
    }

    /**
     * Traverses all nodes colliding with the supplied AABB and their sub-nodes and apply the given operation. It is recursive.
     *
     * @param aabb             The AABB in which to pick the nodes to traverse.
     * @param operationOnNodes The operation to execute on all selected nodes.
     */
    public void traverseNodesRecursive(AABB aabb, Consumer<OctreeNode<E>> operationOnNodes) {
        traverseNodes(aabb, (node) -> {
            node.traverseNodeRecursive(operationOnNodes);
        });
    }

    /**
     * Traverse all the nodes and sub-nodes that intersect with the supplied AABB. It's recursive in the sense that if a node intersects with the given AABB, then at least one of
     * its children also intersects with the AABB and so it will be traversed. Child nodes that do not intersect with the supplied AABB are not traversed.
     *
     * @param aabb             The AABB in which to pick the nodes to traverse.
     * @param operationOnNodes The operation to execute on all selected nodes.
     */
    public void traverseIntersectingNodesRecursive(AABB aabb, Consumer<OctreeNode<E>> operationOnNodes) {
        traverseNodes(aabb, (node) -> {
            node.traverseIntersectingNodeRecursive(aabb, operationOnNodes);
        });
    }

    /**
     * Traverses all entities in all nodes and their sub-nodes intersecting to the supplied AABB and apply the given operation. It is recursive.
     *
     * @param aabb                The AABB in which to pick the nodes to traverse.
     * @param operationOnEntities The operation to execute on the queried entities.
     */
    public void traverseIntersectingNodesEntitiesRecursive(AABB aabb, BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        traverseNodes(aabb, (node) -> {
            node.traverseIntersectingNodeEntitiesRecursive(aabb, operationOnEntities);
        });
    }

    /**
     * Applies the operation on the node at the supplied index and then recursively traverse all sub nodes and do the same. Creates the nodes to traverse if they don't exist.
     *
     * @param index            The index at which the node we want to traverse is.
     * @param operationOnNodes The operation to execute on all nodes.
     */
    private void traverseCreateNodeRecursive(Vector3i index, Consumer<OctreeNode<E>> operationOnNodes) {
        traverseCreateNode(index, (node) -> {
            node.traverseNodeRecursive(operationOnNodes);
        });
    }

    /**
     * Applies the operation on the node at the supplied index and then recursively traverse all sub nodes and do the same. Creates the nodes to traverse if they don't exist.
     *
     * @param aabb             The AABB in which to pick the nodes to traverse.
     * @param operationOnNodes The operation to execute on all nodes.
     */
    private void traverseCreateNodesRecursive(AABB aabb, Consumer<OctreeNode<E>> operationOnNodes) {
        traverseIndices(aabb, (index) -> {
            traverseCreateNodeRecursive(index, operationOnNodes);
        });
    }

    /**
     * Applies the operation on the node at the supplied index. Creates the node to traverse it doesn't exist. It is not recursive.
     *
     * @param index           The index at which the super node we want to traverse is.
     * @param operationOnNode The operation to execute on the nodes at the supplied index.
     */
    private void traverseCreateNode(Vector3i index, Consumer<OctreeNode<E>> operationOnNode) {
        OctreeNode<E> node = m_nodes.get(index);

        if (node == null) {
            m_nodes.put(index, node = new OctreeNode<>(this, index));
        }

        operationOnNode.accept(node);
    }

    /**
     * Applies the operation on the node at the supplied index. Creates the node to traverse if it doesn't exist. It is not recursive.
     *
     * @param aabb             The AABB in which to pick the super nodes to traverse.
     * @param operationOnNodes The operation to execute on all super nodes.
     */
    private void traverseCreateNodes(AABB aabb, Consumer<OctreeNode<E>> operationOnNodes) {
        traverseIndices(aabb, (index) -> {
            traverseCreateNode(index, operationOnNodes);
        });
    }

    /**
     * Applies the operation on maximum depth indices colliding with the AABB. It is not recursive.
     *
     * @param aabb           The AABB in which to pick the indices to traverse.
     * @param traverseMethod The method to call for each index to traverse.
     */
    public void traverseIndices(AABB aabb, Consumer<Vector3i> traverseMethod) {
        int indexMinX = getSuperIndex(aabb.getMin().x);
        int indexMinY = getSuperIndex(aabb.getMin().y);
        int indexMinZ = getSuperIndex(aabb.getMin().z);
        int indexMaxX = getSuperIndex(aabb.getMax().x);
        int indexMaxY = getSuperIndex(aabb.getMax().y);
        int indexMaxZ = getSuperIndex(aabb.getMax().z);

        for (int x = indexMinX; x <= indexMaxX; ++x) {
            for (int y = indexMinY; y <= indexMaxY; ++y) {
                for (int z = indexMinZ; z <= indexMaxZ; ++z) {
                    traverseMethod.accept(new Vector3i(x, y, z));
                }
            }
        }
    }

    /**
     * Applies the operation on the nodes with the maximum depth, a.k.a. the biggest nodes. It is not recursive.
     *
     * @param aabb           The AABB in which to pick the nodes to traverse.
     * @param traverseMethod The method to call for each node to traverse.
     */
    public void traverseNodes(AABB aabb, Consumer<OctreeNode<E>> traverseMethod) {
        traverseIndices(aabb, (index) -> {
            OctreeNode<E> node = m_nodes.get(index);

            if (node != null) {
                traverseMethod.accept(node);
            }
        });
    }

    /**
     * Removes the node at the supplied index, if it exists.
     *
     * @param index The node's index.
     */
    void removeNode(Vector3i index) {
        m_nodes.remove(index);
    }

    /**
     * Adds an entity to the octree.
     *
     * @param entity the entity to add.
     *
     * @return The added entity.
     */
    public E addEntity(E entity) {
        traverseCreateNodes(entity.getAABB(), (node) -> {
            node.addEntity(entity);
        });

        return entity;
    }

    /**
     * Removes the entity from the octree.
     *
     * @param entity The entity to remove
     *
     * @return True if the entity was actually remove, false otherwise.
     */
    public boolean removeEntity(E entity) {
        boolean wasRemoved = false;
        int indexMinX = getSuperIndex(entity.getAABB().getMin().x);
        int indexMinY = getSuperIndex(entity.getAABB().getMin().y);
        int indexMinZ = getSuperIndex(entity.getAABB().getMin().z);
        int indexMaxX = getSuperIndex(entity.getAABB().getMax().x);
        int indexMaxY = getSuperIndex(entity.getAABB().getMax().y);
        int indexMaxZ = getSuperIndex(entity.getAABB().getMax().z);

        for (int x = indexMinX; x <= indexMaxX; ++x) {
            for (int y = indexMinY; y <= indexMaxY; ++y) {
                for (int z = indexMinZ; z <= indexMaxZ; ++z) {
                    OctreeNode<E> node = m_nodes.get(new Vector3i(x, y, z));

                    if (node != null) {
                        wasRemoved |= node.removeEntity(entity);
                    }
                }
            }
        }

        return wasRemoved;
    }

    /**
     * Computes the AABB's inclusive minimum depth where it can be stored.
     *
     * @param aabb The AABB to check for its minimum depth.
     *
     * @return The AABB's inclusive minimum depth where it can be stored.
     */
    public int computeAABBMinDepth(AABB aabb) {
        int depth = OctreeNode.MAX_DEPTH + 1;

        while (--depth >= OctreeNode.MIN_DEPTH) {
            if (!doesAABBFitInRelativeDepth(aabb, depth)) {
                depth = Math.min(++depth, OctreeNode.MAX_DEPTH);
                break;
            }
        }

        return depth;
    }

    @Override
    public String toString() {

        StringBuilder build = new StringBuilder();
        build.append("Octree{");

        m_nodes.values().forEach((eOctreeNode -> {
            build.append(eOctreeNode.toTabulatedString());
        }));

        build.append('}');

        return build.toString();
    }
}
