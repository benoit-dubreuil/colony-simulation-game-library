package com.cheesygames.colonysimulation.math.bounding;

import com.cheesygames.colonysimulation.world.World;
import com.jme3.scene.plugins.blender.math.Vector3d;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoxelRay_Tests {

    private static final double DEFAULT_HALF_EXTENT = new World().getMeshGenerator().getHalfExtent();
    private static final int REPEAT_COUNT_HALF_EXTENT = 3;

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
    public void rayCast_distanceZero_oneTraversedVoxel(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setDirection(Vector3d.UNIT_X);

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        assertEquals(1, traversedVoxelCount.get());
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT)
    public void rayCast_distanceZeroOnPositiveBorder_oneTraversedVoxel(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setStart(new Vector3d(m_halfExtent, m_halfExtent, m_halfExtent));
        m_voxelRay.setDirection(Vector3d.UNIT_X);

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        assertEquals(1, traversedVoxelCount.get());
    }

    @RepeatedTest(REPEAT_COUNT_HALF_EXTENT)
    public void rayCast_distanceZeroOnNegativeBorder_oneTraversedVoxel(RepetitionInfo repetitionInfo) {
        m_halfExtent *= repetitionInfo.getCurrentRepetition();
        m_voxelRay.setStart(new Vector3d(-m_halfExtent, -m_halfExtent, -m_halfExtent));
        m_voxelRay.setDirection(Vector3d.UNIT_X);

        AtomicInteger traversedVoxelCount = new AtomicInteger();

        m_voxelRay.rayCast(m_halfExtent, (voxelIndex) -> {
            traversedVoxelCount.incrementAndGet();
            return false;
        });

        assertEquals(1, traversedVoxelCount.get());
    }
}
