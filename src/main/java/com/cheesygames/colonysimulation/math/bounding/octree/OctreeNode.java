package com.cheesygames.colonysimulation.math.bounding.octree;

import com.cheesygames.colonysimulation.math.bounding.AABB;
import com.cheesygames.colonysimulation.math.direction.Direction3D;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.jme3.math.Vector3f;
import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * An octree node. It is mainly specified by its index, depth and parent. It holds entities and may have children.
 *
 * @param <E> Entity Type.
 */
public class OctreeNode<E extends IOctreeEntity> {

    /**
     * A depth of 0 means an AABB of 1x1x1.
     */
    static final int MIN_DEPTH = 0;
    static final int MIN_DEPTH_LENGTH = 1 << MIN_DEPTH;

    /**
     * A depth of 6 means an AABB of 64x64x64.
     */
    static final int MAX_DEPTH = 6;
    static final int MAX_DEPTH_LENGTH = 1 << MAX_DEPTH;

    private static final int MAX_ENTITY_THRESHOLD = 3;
    SetUniqueList<E> m_entities;
    private Octree<E> m_octree;
    private OctreeNode m_parent;
    private int m_depth;
    /**
     * The index of the super parent indirectly or directly holding this node. The super parent has the maximum depth.
     */
    private Vector3i m_index;
    /**
     * The relative direction according to its parent center. The direction a is one of the array value from the DIAGONALS_3D array.
     */
    private Direction3D m_direction;
    private AABB m_aabb;
    private OctreeNode[] m_children;

    /**
     * Constructor for super parent octree nodes. A super parent has the maximum depth.
     *
     * @param index The index at which resides the super parent octree node in the octree.
     */
    public OctreeNode(Octree<E> octree, Vector3i index) {
        this.m_octree = octree;
        this.m_parent = null;
        this.m_depth = MAX_DEPTH;
        this.m_index = index;

        Vector3i directionVector = new Vector3i(index);
        if (directionVector.x == 0) {
            directionVector.setX(1);
        }
        if (directionVector.y == 0) {
            directionVector.setY(1);
        }
        if (directionVector.z == 0) {
            directionVector.setZ(1);
        }

        this.m_direction = Direction3D.findDirectionFromVector(directionVector);
        this.m_aabb = computeAABB();
        this.m_entities = SetUniqueList.setUniqueList(new ArrayList<>());
    }

    /**
     * Constructor for an octree node that is also a child of another octree node.
     *
     * @param parent    The parent node.
     * @param direction The relative according to its parent center.
     */
    public OctreeNode(OctreeNode parent, Direction3D direction) {
        this.m_octree = parent.m_octree;
        this.m_parent = parent;
        this.m_depth = parent.m_depth - 1;
        this.m_index = parent.m_index;
        this.m_direction = direction;
        this.m_aabb = computeAABB();
        this.m_entities = SetUniqueList.setUniqueList(new ArrayList<>());
    }

    /**
     * Creates the children but doesn't set the member variables m_children.
     *
     * @return Newly created children.
     */
    private OctreeNode[] createChildren() {
        OctreeNode[] children = null;

        if (m_depth > 0) {
            children = new OctreeNode[Direction3D.DIAGONALS_3D.length];

            for (int i = 0; i < Direction3D.DIAGONALS_3D.length; ++i) {
                children[i] = new OctreeNode(this, Direction3D.DIAGONALS_3D[i]);
            }
        }

        return children;
    }

    /**
     * Traverses all entities in this node and in all its children nodes. It is recursive.
     *
     * @param operationOnEntities The operation to execute on the queried entities.
     */
    public void traverseNodeEntitiesRecursive(BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        for (E entity : m_entities) {
            operationOnEntities.accept(this, entity);
        }

        if (m_children != null) {
            for (OctreeNode<E> child : m_children) {
                child.traverseNodeEntitiesRecursive(operationOnEntities);
            }
        }
    }

    /**
     * Traverses all entities in this node and in all its children nodes. It is recursive.
     *
     * @param operationOnEntities The operation to execute on the queried entities.
     * @param minDepth            The inclusive minimum depth. Searching for entities in lower depths than this one is unnecessary.
     */
    public void traverseNodeEntitiesRecursive(int minDepth, BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        if (m_depth >= minDepth) {
            for (E entity : m_entities) {
                operationOnEntities.accept(this, entity);
            }

            if (m_children != null && m_depth > minDepth) {
                for (OctreeNode<E> child : m_children) {
                    child.traverseNodeEntitiesRecursive(operationOnEntities);
                }
            }
        }
    }

    /**
     * Traverses all entities in this node and in all its children nodes that intersect with the supplied AABB. It is recursive.
     *
     * @param operationOnEntities The operation to execute on the queried entities.
     * @param aabb                The AABB to intersect the sub-nodes with.
     */
    public void traverseIntersectingNodeEntitiesRecursive(AABB aabb, BiConsumer<OctreeNode<E>, E> operationOnEntities) {
        for (E entity : m_entities) {
            operationOnEntities.accept(this, entity);
        }

        if (m_children != null) {
            for (OctreeNode<E> child : m_children) {
                if (child.m_aabb.isIntersecting(aabb)) {
                    child.traverseIntersectingNodeEntitiesRecursive(aabb, operationOnEntities);
                }
            }
        }
    }

    /**
     * Applies the operation on this node and then recursively traverse all sub-nodes and do the same.
     *
     * @param operationOnNodes The operation to execute on all nodes.
     */
    public void traverseNodeRecursive(Consumer<OctreeNode<E>> operationOnNodes) {
        operationOnNodes.accept(this);

        if (m_children != null) {
            for (OctreeNode<E> child : m_children) {
                child.traverseNodeRecursive(operationOnNodes);
            }
        }
    }

    /**
     * Applies the operation on this node and then recursively traverse all sub-nodes that intersect with the supplied AABB and do the same.
     *
     * @param operationOnNodes The operation to execute on all nodes.
     * @param aabb             The AABB to intersect the sub-nodes with.
     */
    public void traverseIntersectingNodeRecursive(AABB aabb, Consumer<OctreeNode<E>> operationOnNodes) {
        operationOnNodes.accept(this);

        if (m_children != null) {
            for (OctreeNode<E> child : m_children) {
                if (child.m_aabb.isIntersecting(aabb)) {
                    child.traverseIntersectingNodeRecursive(aabb, operationOnNodes);
                }
            }
        }
    }

    /**
     * Adds an entity to this octree node. If the entity can fit into a smaller node, then it will be added to the smaller node. This method does not verify if the entity should
     * actually be added to this node.
     *
     * @param entity The entity to add.
     *
     * @return The added entity.
     */
    public E addEntity(E entity) {
        if (m_depth == 0) {
            m_entities.add(entity);
        }
        else {
            if (m_entities.size() == MAX_ENTITY_THRESHOLD) {
                fitEntitiesUnderThresholdIndex();
            }

            if ((m_entities.size() >= MAX_ENTITY_THRESHOLD || m_children != null) && doesAABBFitInChildren(entity.getAABB())) {
                if (m_children == null) {
                    m_children = createChildren();
                }

                m_children[computeChildIndexFromAABB(entity.getAABB())].addEntity(entity);
            }
            else {
                m_entities.add(entity);
            }
        }

        return entity;
    }

    /**
     * Fits the entities inclusively under the maximum entity threshold. This will check if those entities fits in any child node. If yes, it will then add them to their respective
     * child node and then remove them from this parent. If no, then it doesn't do anything.
     */
    private void fitEntitiesUnderThresholdIndex() {
        Iterator<E> it = m_entities.iterator();
        int indexBeforeThreshold = 0;

        while (it.hasNext() && indexBeforeThreshold <= MAX_ENTITY_THRESHOLD) {
            E entityToFitAgain = it.next();

            if (doesAABBFitInChildren(entityToFitAgain.getAABB())) {
                if (m_children == null) {
                    m_children = createChildren();
                }

                m_children[computeChildIndexFromAABB(entityToFitAgain.getAABB())].addEntity(entityToFitAgain);
                it.remove();
            }

            ++indexBeforeThreshold;
        }
    }

    /**
     * Removes an entity from this octree node. If the entity removal made the children of a node empty, then the children nodes will be deleted.
     *
     * @param entity The entity to remove.
     *
     * @return If the removal was successful, i.e. if the entity was actually in this node or in any sub-node.
     */
    public boolean removeEntity(E entity) {
        boolean result;

        if (m_depth > 0 && m_children != null && doesAABBFitInChildren(entity.getAABB())) {
            result = m_children[computeChildIndexFromAABB(entity.getAABB())].removeEntity(entity);
        }
        else {
            result = m_entities.remove(entity);

            if (m_parent != null) {
                m_parent.removeChildrenIfEmpty();
            }
            else if (m_children == null && isEmpty()) {
                m_octree.removeNode(m_index);
                m_octree = null;
            }
        }

        return result;
    }

    /**
     * Removes the children nodes if they are all empty in terms of entities.
     */
    private void removeChildrenIfEmpty() {
        if (m_children != null) {
            for (OctreeNode<E> child : m_children) {
                if (!child.isEmpty() && child.m_children != null) {
                    return;
                }
            }

            for (OctreeNode<E> child : m_children) {
                child.m_parent = null;
                child.m_octree = null;
            }
            m_children = null;
        }
    }

    /**
     * Computes the child index for the AABB, i.e. in which child should the AABB be stored into.
     * <p>
     * Should only be called if {@link #doesAABBFitInChildren(AABB)} returns true.
     *
     * @param aabb The AABB to know in which child to be stored.
     *
     * @return The child index in which the AABB should be stored.
     */
    private int computeChildIndexFromAABB(AABB aabb) {
        Vector3i unnormalizedDirection = new Vector3i();
        for (int componentIndex = 0; componentIndex < Vector3i.COMPONENT_COUNT; ++componentIndex) {
            int component = (int) (aabb.getMin().get(componentIndex) - m_aabb.getCenter().get(componentIndex));

            if (component == 0) {
                component = (int) (aabb.getMax().get(componentIndex) - m_aabb.getCenter().get(componentIndex));
            }

            unnormalizedDirection.set(componentIndex, component);
        }

        // Relative indices
        return Direction3D.findDirectionFromVector(unnormalizedDirection).getDiagonal3DIndex();
    }

    /**
     * Checks whether the supplied AABB fits in any of the children exclusively.
     *
     * @param aabb The AABB to check whether it fits in any of the children exclusively.
     *
     * @return True if it fits in any of the children exclusively, false otherwise.
     */
    private boolean doesAABBFitInChildren(AABB aabb) {
        return Octree.doesAABBFitInDepth(aabb, m_index, m_depth - 1);
    }

    /**
     * Computes the AABB of this octree node. It represents the bounding volume of the node.
     *
     * @return A new AABB.
     */
    private AABB computeAABB() {
        float radius = (1 << m_depth) / 2f;
        Vector3f parentCenter;

        if (m_parent != null) {
            parentCenter = m_parent.m_aabb.getCenter();
        }
        else {
            parentCenter = m_index.toVector3f().mult(radius);
            if (m_index.x < 0) {
                parentCenter.x += radius;
            }
            if (m_index.y < 0) {
                parentCenter.y += radius;
            }
            if (m_index.z < 0) {
                parentCenter.z += radius;
            }
        }

        Vector3f center = new Vector3f(parentCenter.x + radius * m_direction.getDirectionX(),
            parentCenter.y + radius * m_direction.getDirectionY(),
            parentCenter.z + radius * m_direction.getDirectionZ());

        return new AABB(center, radius);
    }

    /**
     * Checks if this node directly contains the supplied entity. It is not recursive.
     *
     * @param entity The entity to look for.
     *
     * @return True if the entity is present in this node, false otherwise.
     */
    public boolean contains(E entity) {
        return m_entities.contains(entity);
    }

    private String padToDepth() {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < Math.abs(m_depth - MAX_DEPTH); i++) {
            tabs.append('\t');
        }
        return tabs.toString();
    }

    public String toTabulatedString() {
        StringBuilder builder = new StringBuilder();

        builder.append('\n')
               .append('\t')
               .append(padToDepth())
               .append("OctreeNode{")
               .append("depth=")
               .append(m_depth)
               .append(", index=")
               .append(m_index)
               .append(", direction=")
               .append(m_direction)
               .append(", aabb=")
               .append(m_aabb)
               .append(", entities count=")
               .append(m_entities.size())
               .append(", children=");

        if (m_children != null) {
            Arrays.stream(m_children).forEach(child -> {
                builder.append(padToDepth()).append(child.toTabulatedString());
            });
            builder.append('}');
        }
        else {
            builder.append("null").append('}');
        }

        return builder.toString();
    }

    /**
     * Checks if it's empty; if it does not directly contain any entity. Does not check its children.
     *
     * @return True if this node is directly empty, false otherwise.
     */
    public boolean isEmpty() {
        return m_entities.isEmpty();
    }

    @Override
    public String toString() {
        return "OctreeNode{" + '\n' + "depth=" + m_depth + ", index=" + m_index + ", direction=" + m_direction + ", aabb=" + m_aabb + ", entities count=" + m_entities.size()
            + ", children=" + Arrays.toString(m_children) + '}';
    }
}
