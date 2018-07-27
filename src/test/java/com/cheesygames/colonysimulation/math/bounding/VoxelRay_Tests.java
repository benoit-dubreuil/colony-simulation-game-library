package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.math.MathExt;
import com.cheesygames.colonysimulation.world.World;
import com.jme3.scene.plugins.blender.math.Vector3d;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoxelRay_Tests {

    private static final double DEFAULT_HALF_EXTENT = new World().getMeshGenerator().getHalfExtent();
    private static final int REPEAT_COUNT_HALF_EXTENT = 3;
    private static final int VECTOR3_COMPONENT_COUNT = 3;
    private static final int DIAGONAL_COUNT = 8;

    private double m_halfExtent;
    private VoxelRay m_voxelRay;

    @BeforeAll
    public void init() {
        m_voxelRay = new VoxelRay();
    }

    @BeforeEach
    public void setup() {
        m_halfExtent = DEFAULT_HALF_EXTENT;

        m_voxelRay.setStart(Vector3d.ZERO);
        m_voxelRay.setDirection(Vector3d.ZERO);
        m_voxelRay.setLength(0);
    }

    @Test
    public void rayCast_halfExtentNaN_assertError() {
        m_halfExtent = Double.NaN;
        assertThrows(AssertionError.class, () -> m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> false));
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT)
    public void rayCast_startNaN_assertError(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setStart(new Vector3d(Double.NaN, Double.NaN, Double.NaN));

        assertThrows(AssertionError.class, () -> m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> false));
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT)
    public void rayCast_directionNaN_assertError(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setDirection(new Vector3d(Double.NaN, Double.NaN, Double.NaN));

        assertThrows(AssertionError.class, () -> m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> false));
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT)
    public void rayCast_lengthNaN_assertError(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setLength(Double.NaN);

        assertThrows(AssertionError.class, () -> m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> false));
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT)
    public void rayCast_distanceZero_1TraversedVoxel(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setDirection(Vector3d.UNIT_X);
        m_voxelRay.getDirection().normalizeLocal();

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        assertEquals(1, traversedVoxelCount.get());
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT)
    public void rayCast_distanceZeroOnPositiveBorder_1TraversedVoxel(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setStart(new Vector3d(m_halfExtent, m_halfExtent, m_halfExtent));
        m_voxelRay.setDirection(Vector3d.UNIT_X);
        m_voxelRay.getDirection().normalizeLocal();

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        // Expected 1 because the length is 0, however we still count the current voxel.
        assertEquals(1, traversedVoxelCount.get());
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT)
    public void rayCast_distanceZeroOnNegativeBorder_1TraversedVoxel(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setStart(new Vector3d(-m_halfExtent, -m_halfExtent, -m_halfExtent));
        m_voxelRay.setDirection(Vector3d.UNIT_X);
        m_voxelRay.getDirection().normalizeLocal();

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        // Expected 1 because the length is 0, however we still count the current voxel.
        assertEquals(1, traversedVoxelCount.get());
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT * VECTOR3_COMPONENT_COUNT)
    public void rayCast_halfExtentLength3Directions_2TraversedVoxels(RepetitionInfo repetitionInfo) {
        final int currentRepetitionPlusVecComponentCount = repetitionInfo.getCurrentRepetition() + VECTOR3_COMPONENT_COUNT - 1;

        m_halfExtent *= currentRepetitionPlusVecComponentCount / VECTOR3_COMPONENT_COUNT;
        m_voxelRay.setLength(m_halfExtent);
        m_voxelRay.getDirection().set((repetitionInfo.getCurrentRepetition() - 1) % REPEAT_COUNT_HALF_EXTENT, 1.0);
        m_voxelRay.getDirection().normalizeLocal();

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        // Expected the 2 because of unary positive direction. Upper bounds are exclusive.
        assertEquals(2, traversedVoxelCount.get());
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT * VECTOR3_COMPONENT_COUNT)
    public void rayCast_3length3Directions_4TraversedVoxels(RepetitionInfo repetitionInfo) {
        final int length = 3;
        final int currentRepetitionPlusVecComponentCount = repetitionInfo.getCurrentRepetition() + VECTOR3_COMPONENT_COUNT - 1;

        m_halfExtent *= currentRepetitionPlusVecComponentCount / VECTOR3_COMPONENT_COUNT;
        m_voxelRay.setLength(length * (currentRepetitionPlusVecComponentCount / VECTOR3_COMPONENT_COUNT));
        m_voxelRay.getDirection().set((repetitionInfo.getCurrentRepetition() - 1) % REPEAT_COUNT_HALF_EXTENT, 1.0);
        m_voxelRay.getDirection().normalizeLocal();

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        // Expected the length + 1 because of unary positive direction. Upper bounds are exclusive.
        assertEquals(length + 1, traversedVoxelCount.get());
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT * VECTOR3_COMPONENT_COUNT)
    public void rayCast_3length3NegativeDirections_4TraversedVoxels(RepetitionInfo repetitionInfo) {
        final int length = 3;
        final int currentRepetitionPlusVecComponentCount = repetitionInfo.getCurrentRepetition() + VECTOR3_COMPONENT_COUNT - 1;

        m_halfExtent *= currentRepetitionPlusVecComponentCount / VECTOR3_COMPONENT_COUNT;
        m_voxelRay.setLength(length * (currentRepetitionPlusVecComponentCount / VECTOR3_COMPONENT_COUNT));
        m_voxelRay.getDirection().set((repetitionInfo.getCurrentRepetition() - 1) % REPEAT_COUNT_HALF_EXTENT, -1.0);
        m_voxelRay.getDirection().normalizeLocal();

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        // Expected the length + 1 because of unary negative direction and because the zero voxel is included. Lower bounds are inclusive.
        assertEquals(length + 1, traversedVoxelCount.get());
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT * DIAGONAL_COUNT)
    public void rayCast_3lengthAllDiagonalDirections_7TraversedVoxels(RepetitionInfo repetitionInfo) {
        final int length = 3;
        final int currentRepetitionPlusVecComponentCount = repetitionInfo.getCurrentRepetition() + DIAGONAL_COUNT - 1;

        int binaryDirection = (repetitionInfo.getCurrentRepetition() - 1) % DIAGONAL_COUNT;
        m_voxelRay.getDirection().x = MathExt.getSignZeroPositive((binaryDirection >>> 2) - 1);
        m_voxelRay.getDirection().y = MathExt.getSignZeroPositive(((binaryDirection & (1 << 1)) >>> 1) - 1);
        m_voxelRay.getDirection().z = MathExt.getSignZeroPositive((binaryDirection & 1) - 1);
        m_voxelRay.getDirection().normalizeLocal();

        m_halfExtent *= currentRepetitionPlusVecComponentCount / DIAGONAL_COUNT;
        m_voxelRay.setLength(length * (currentRepetitionPlusVecComponentCount / DIAGONAL_COUNT));

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        assertEquals(7, traversedVoxelCount.get());
    }
}
