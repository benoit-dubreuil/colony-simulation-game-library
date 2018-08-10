package com.cheesygames.colonysimulation.world.generation;

import com.cheesygames.colonysimulation.math.vector.Vector3i;
import com.cheesygames.colonysimulation.world.chunk.voxel.Voxel;
import com.cheesygames.colonysimulation.world.chunk.voxel.VoxelType;
import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleScaleDomain;

/**
 * A {@link IWorldGenerator} that generates voxels according to a 3D mountainous coherent noise module.
 */
public class MountainousWorldGenerator implements IWorldGenerator {

    private Module m_generatorModule;

    public MountainousWorldGenerator() {
        ModuleBasisFunction basis = new ModuleBasisFunction();
        basis.setType(ModuleBasisFunction.BasisType.SIMPLEX);

        ModuleAutoCorrect correct = new ModuleAutoCorrect();
        correct.setSource(basis);
        correct.calculate3D();

        ModuleScaleDomain scaleDomain = new ModuleScaleDomain();
        scaleDomain.setSource(correct);
        scaleDomain.setScaleX(1.0 / 50);
        scaleDomain.setScaleY(1.0 / 50);
        scaleDomain.setScaleZ(1.0 / 50);

        m_generatorModule = scaleDomain;
    }

    @Override
    public void generateWorld() {
        Vector3i minIndex = new Vector3i(-2, -2, -2);
        Vector3i maxIndex = new Vector3i(2, 1, 2);

        for (int x = minIndex.x; x <= maxIndex.x; ++x) {
            for (int y = minIndex.y; y <= maxIndex.y; ++y) {
                for (int z = minIndex.z; z <= maxIndex.z; ++z) {
                    generateChunk(new Vector3i(x, y, z));
                }
            }
        }
    }

    @Override
    public Voxel generateVoxel(int x, int y, int z) {
        return m_generatorModule.get(x, y, z) >= 0.5f ? new Voxel(VoxelType.SOLID) : new Voxel(VoxelType.AIR);
    }
}
