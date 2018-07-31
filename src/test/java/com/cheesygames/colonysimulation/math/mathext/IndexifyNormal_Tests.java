package com.cheesygames.colonysimulation.math.mathext;

import com.cheesygames.colonysimulation.math.MathExt;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the method {@link MathExt#indexifyNormal(int)} and all its overloads, if they exist.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IndexifyNormal_Tests {

    private int m_number;
    private Random m_random;

    @BeforeAll
    public void init() {
        m_random = new Random();
    }

    @BeforeEach
    public void setup() {
        m_random.setSeed(0);
    }

    @Test
    public void minusOne_zero() {
        m_number = -1;
        assertEquals(0, MathExt.indexifyNormal(m_number));
    }

    @Test
    public void minimum_assertError() {
        m_number = Integer.MIN_VALUE;
        assertThrows(AssertionError.class, () -> MathExt.indexifyNormal(m_number));
    }

    @Test
    public void zero_zero() {
        m_number = 0;
        assertEquals(0, MathExt.indexifyNormal(m_number));
    }

    @Test
    public void one_one() {
        m_number = 1;
        assertEquals(1, MathExt.indexifyNormal(m_number));
    }

    @Test
    public void maximum_one() {
        m_number = Integer.MAX_VALUE;
        assertEquals(1, MathExt.indexifyNormal(m_number));
    }

    @TestFactory
    public Collection<DynamicTest> nonZeroPositive_one() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            m_number = m_random.nextInt(Integer.MAX_VALUE) + 1;

            tests.add(DynamicTest.dynamicTest("test_" + i, () -> {
                assertEquals(1, MathExt.indexifyNormal(m_number));
            }));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> negative_zero() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            m_number = -m_random.nextInt(Integer.MAX_VALUE) + 1;

            tests.add(DynamicTest.dynamicTest("test_" + i, () -> {
                assertEquals(0, MathExt.indexifyNormal(m_number));
            }));
        }

        return tests;
    }
}
