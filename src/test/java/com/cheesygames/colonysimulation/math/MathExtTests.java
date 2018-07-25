package com.cheesygames.colonysimulation.math;

import org.junit.jupiter.api.*;

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
    public void abs_intMaxInt_positive() {
        assertEquals(Integer.MAX_VALUE, MathExt.abs(Integer.MAX_VALUE));
        assertTrue(MathExt.abs(Integer.MAX_VALUE) >= 0);
    }

    /**
     * The minimum integer does not have positive equivalent integer because the number zero counts as a positive number and thus the maximum integer is the absolute negative
     * number + 1.
     */
    @Test
    public void abs_intMinInt_negative() {
        assertEquals(Integer.MIN_VALUE, MathExt.abs(Integer.MIN_VALUE));
        assertTrue(MathExt.abs(Integer.MIN_VALUE) < 0);
    }
}
