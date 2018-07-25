package com.cheesygames.colonysimulation.math;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MathExtTests {

    private Random m_random;

    @BeforeAll
    public void init() {
        m_random = new Random();
    }

    @BeforeEach
    public void setup() {
        m_random.setSeed(0);
    }

    @TestFactory
    public Stream<DynamicTest> abs_intZeroPositive_positive() {
        return IntStream.iterate(0, n -> n = m_random.nextInt(Integer.MAX_VALUE)).limit(10).mapToObj(n -> DynamicTest.dynamicTest("test_" + n, () -> {
            assertEquals(n, MathExt.abs(n));
            assertTrue(MathExt.abs(n) >= 0);
        }));
    }

    @TestFactory
    public Stream<DynamicTest> abs_intNegative_positive() {
        return IntStream.iterate(-1, n -> n = -(m_random.nextInt(Integer.MAX_VALUE) + 1)).limit(10).mapToObj(n -> DynamicTest.dynamicTest("test_" + n, () -> {
            assertNotEquals(n, MathExt.abs(n));
            assertTrue(MathExt.abs(n) >= 0);
        }));
    }

    @Test
    public void abs_intMax_positive() {
        assertEquals(Integer.MAX_VALUE, MathExt.abs(Integer.MAX_VALUE));
        assertTrue(MathExt.abs(Integer.MAX_VALUE) >= 0);
    }


    /**
     * The minimum integer does not have positive equivalent integer because the number zero counts as a positive number and thus the maximum integer is the absolute negative
     * number + 1.
     */
    @Test
    public void abs_intMin_negative() {
        assertEquals(Integer.MIN_VALUE, MathExt.abs(Integer.MIN_VALUE));
        assertTrue(MathExt.abs(Integer.MIN_VALUE) < 0);
    }

    @TestFactory
    public Collection<DynamicTest> abs_floatZeroPositive_positive() {
        List<DynamicTest> tests = new ArrayList<>();

        tests.add(DynamicTest.dynamicTest("test_0", () -> {
            assertEquals(0f, MathExt.abs(0f));
            assertTrue(MathExt.abs(0f) == 0f);
        }));

        for (int i = 1; i < 10; ++i) {
            float number = m_random.nextFloat() * Float.MAX_VALUE;

            tests.add(DynamicTest.dynamicTest("test_" + i, () -> {
                assertEquals(number, MathExt.abs(number));
                assertTrue(MathExt.abs(number) == number);
            }));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> abs_floatNegative_positive() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            float number = -m_random.nextFloat() * Float.MAX_VALUE;

            tests.add(DynamicTest.dynamicTest("test_" + i, () -> {
                assertEquals(-number, MathExt.abs(number));
                assertTrue(MathExt.abs(number) == -number);
            }));
        }

        return tests;
    }

    @Test
    public void abs_floatPositiveInfinity_positive() {
        assertEquals(Float.POSITIVE_INFINITY, MathExt.abs(Float.POSITIVE_INFINITY));
        assertTrue(MathExt.abs(Float.POSITIVE_INFINITY) >= 0);
    }

    @Test
    public void abs_floatNegativeInfinity_positive() {
        assertEquals(-Float.NEGATIVE_INFINITY, MathExt.abs(Float.NEGATIVE_INFINITY));
        assertTrue(MathExt.abs(Float.NEGATIVE_INFINITY) >= 0);
    }

    @Test
    public void abs_floatNaN_equals() {
        assertEquals(Float.NaN, MathExt.abs(Float.NaN));
    }

    @Test
    public void abs_floatMaxPositive_positive() {
        assertEquals(Float.MAX_VALUE, MathExt.abs(Float.MAX_VALUE));
        assertTrue(MathExt.abs(Float.MAX_VALUE) >= 0);
    }

    @Test
    public void abs_floatMinPositive_positive() {
        assertEquals(Float.MIN_VALUE, MathExt.abs(Float.MIN_VALUE));
        assertTrue(MathExt.abs(Float.MIN_VALUE) >= 0);
    }

    @Test
    public void abs_floatMaxNegative_positive() {
        assertEquals(Float.MAX_VALUE, MathExt.abs(-Float.MAX_VALUE));
        assertTrue(MathExt.abs(-Float.MAX_VALUE) >= 0);
    }

    @Test
    public void abs_floatMinNegative_positive() {
        assertEquals(Float.MIN_VALUE, MathExt.abs(-Float.MIN_VALUE));
        assertTrue(MathExt.abs(-Float.MIN_VALUE) >= 0);
    }
}
