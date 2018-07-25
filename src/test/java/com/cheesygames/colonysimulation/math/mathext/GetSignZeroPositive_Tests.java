package com.cheesygames.colonysimulation.math.mathext;

import com.cheesygames.colonysimulation.math.MathExt;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the method {@link MathExt#getSignZeroPositive(int)} and all its overloads, if they exist.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetSignZeroPositive_Tests {

    private static final int NEGATIVE_SIGN = -1;
    private static final int POSITIVE_SIGN = 1;

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
    public void intMinusOne_negativeSign() {
        m_number = -1;
        assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(m_number));
    }

    @Test
    public void intMinimum_negativeSign() {
        m_number = Integer.MIN_VALUE;
        assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(m_number));
    }

    @Test
    public void intZero_positiveSign() {
        m_number = 0;
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(m_number));
    }

    @Test
    public void intOne_positiveSign() {
        m_number = 1;
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(m_number));
    }

    @Test
    public void intMaximum_positiveSign() {
        m_number = Integer.MAX_VALUE;
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(m_number));
    }

    @TestFactory
    public Collection<DynamicTest> intNonZeroPositive_positiveSign() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            m_number = m_random.nextInt(Integer.MAX_VALUE) + 1;

            tests.add(DynamicTest.dynamicTest("test_" + i, () -> {
                assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(m_number));
            }));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> intNegative_negativeSign() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            m_number = -m_random.nextInt(Integer.MAX_VALUE) + 1;

            tests.add(DynamicTest.dynamicTest("test_" + i, () -> {
                assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(m_number));
            }));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> floatZeroPositive_positiveSign() {
        List<DynamicTest> tests = new ArrayList<>();

        tests.add(DynamicTest.dynamicTest("test_0", () -> assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(0f))));

        for (int i = 1; i < 10; ++i) {
            float number = m_random.nextFloat() * Float.MAX_VALUE;
            tests.add(DynamicTest.dynamicTest("test_" + i, () -> assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(number))));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> floatNegative_negativeSign() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            float number = -m_random.nextFloat() * Float.MAX_VALUE;
            tests.add(DynamicTest.dynamicTest("test_" + i, () -> assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(number))));
        }

        return tests;
    }

    @Test
    public void floatPositiveInfinity_positiveSign() {
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(Float.POSITIVE_INFINITY));
    }

    @Test
    public void floatNegativeInfinity_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(Float.NEGATIVE_INFINITY));
    }

    @Test
    public void floatNaN_positiveSign() {
        assertThrows(AssertionError.class, () ->  MathExt.getSignZeroPositive(Float.NaN));
    }

    @Test
    public void floatMaxPositive_positiveSign() {
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(Float.MAX_VALUE));
    }

    @Test
    public void floatMinPositive_positiveSign() {
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(Float.MIN_VALUE));
    }

    @Test
    public void floatMaxNegative_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(-Float.MAX_VALUE));
    }

    @Test
    public void floatMinNegative_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(-Float.MIN_VALUE));
    }

    @TestFactory
    public Collection<DynamicTest> doubleZeroPositive_positiveSign() {
        List<DynamicTest> tests = new ArrayList<>();

        tests.add(DynamicTest.dynamicTest("test_0", () -> assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(0f))));

        for (int i = 1; i < 10; ++i) {
            double number = m_random.nextDouble() * Double.MAX_VALUE;
            tests.add(DynamicTest.dynamicTest("test_" + i, () -> assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(number))));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> doubleNegative_negativeSign() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            double number = -m_random.nextDouble() * Double.MAX_VALUE;
            tests.add(DynamicTest.dynamicTest("test_" + i, () -> assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(number))));
        }

        return tests;
    }

    @Test
    public void doublePositiveInfinity_positiveSign() {
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(Double.POSITIVE_INFINITY));
    }

    @Test
    public void doubleNegativeInfinity_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void doubleNaN_positiveSign() {
        assertThrows(AssertionError.class, () ->  MathExt.getSignZeroPositive(Double.NaN));
    }

    @Test
    public void doubleMaxPositive_positiveSign() {
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(Double.MAX_VALUE));
    }

    @Test
    public void doubleMinPositive_positiveSign() {
        assertEquals(POSITIVE_SIGN, MathExt.getSignZeroPositive(Double.MIN_VALUE));
    }

    @Test
    public void doubleMaxNegative_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(-Double.MAX_VALUE));
    }

    @Test
    public void doubleMinNegative_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getSignZeroPositive(-Double.MIN_VALUE));
    }
}
