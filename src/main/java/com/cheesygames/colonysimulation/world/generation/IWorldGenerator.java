package com.cheesygames.colonysimulation.world.generation;

import com.cheesygames.colonysimulation.GameGlobal;
import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.Chunk;
import com.cheesygames.colonysimulation.world.chunk.voxel.Voxel;

/**
 * Interface for world generators. Allows the user to generate a world, a chunk and a simple voxel.
 */
public interface IWorldGenerator {

    void generateWorld();

    /**
     * Creates a chunk and fill it with generated value but does not add it to the world.
     *
     * @param index The chunk's index. This parameter will be kept as reference within the newly created and generated chunk.
     *
     * @return A newly created and generated chunk.
     */
    default Chunk createChunk(Vector3i index) {
        Chunk chunk = new Chunk(index);
        chunk.generateData(this);

        return chunk;
    }

    /**
     * Generates a chunk at the specified index and add it to the world if it's not empty.
     *
     * @param index The chunk's index. This parameter will be kept as reference within the newly created and generated chunk.
     *
     * @return The newly created chunk, whether it is added to the world or not.
     */
    default Chunk generateChunk(Vector3i index) {
        Chunk chunk = createChunk(index);
        GameGlobal.world.addChunk(chunk);

        return chunk;
    }

    Voxel generateVoxel(int x, int y, int z);
}
