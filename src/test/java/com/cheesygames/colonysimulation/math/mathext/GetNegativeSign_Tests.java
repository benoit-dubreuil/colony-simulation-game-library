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
 * Tests for the method {@link MathExt#getNegativeSign(int)} and all its overloads, if they exist.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetNegativeSign_Tests {

    private static final int NEGATIVE_SIGN = -1;
    private static final int ZERO_SIGN = 0;

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
        assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(m_number));
    }

    @Test
    public void intMinimum_negativeSign() {
        m_number = Integer.MIN_VALUE;
        assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(m_number));
    }

    @Test
    public void intZero_zeroSign() {
        m_number = 0;
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(m_number));
    }

    @Test
    public void intOne_zeroSign() {
        m_number = 1;
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(m_number));
    }

    @Test
    public void intMaximum_zeroSign() {
        m_number = Integer.MAX_VALUE;
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(m_number));
    }

    @TestFactory
    public Collection<DynamicTest> intNonZeroPositive_zeroSign() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            m_number = m_random.nextInt(Integer.MAX_VALUE) + 1;

            tests.add(DynamicTest.dynamicTest("test_" + i, () -> {
                assertEquals(ZERO_SIGN, MathExt.getNegativeSign(m_number));
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
                assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(m_number));
            }));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> floatZeroPositive_zeroSign() {
        List<DynamicTest> tests = new ArrayList<>();

        tests.add(DynamicTest.dynamicTest("test_0", () -> assertEquals(ZERO_SIGN, MathExt.getNegativeSign(0f))));

        for (int i = 1; i < 10; ++i) {
            float number = m_random.nextFloat() * Float.MAX_VALUE;
            tests.add(DynamicTest.dynamicTest("test_" + i, () -> assertEquals(ZERO_SIGN, MathExt.getNegativeSign(number))));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> floatNegative_negativeSign() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            float number = -m_random.nextFloat() * Float.MAX_VALUE;
            tests.add(DynamicTest.dynamicTest("test_" + i, () -> assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(number))));
        }

        return tests;
    }

    @Test
    public void floatPositiveInfinity_zeroSign() {
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(Float.POSITIVE_INFINITY));
    }

    @Test
    public void floatNegativeInfinity_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(Float.NEGATIVE_INFINITY));
    }

    @Test
    public void floatNaN_assertError() {
        assertThrows(AssertionError.class, () -> MathExt.getNegativeSign(Float.NaN));
    }

    @Test
    public void floatMaxPositive_zeroSign() {
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(Float.MAX_VALUE));
    }

    @Test
    public void floatMinPositive_zeroSign() {
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(Float.MIN_VALUE));
    }

    @Test
    public void floatMaxNegative_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(-Float.MAX_VALUE));
    }

    @Test
    public void floatMinNegative_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(-Float.MIN_VALUE));
    }

    @TestFactory
    public Collection<DynamicTest> doubleZeroPositive_zeroSign() {
        List<DynamicTest> tests = new ArrayList<>();

        tests.add(DynamicTest.dynamicTest("test_0", () -> assertEquals(ZERO_SIGN, MathExt.getNegativeSign(0f))));

        for (int i = 1; i < 10; ++i) {
            double number = m_random.nextDouble() * Double.MAX_VALUE;
            tests.add(DynamicTest.dynamicTest("test_" + i, () -> assertEquals(ZERO_SIGN, MathExt.getNegativeSign(number))));
        }

        return tests;
    }

    @TestFactory
    public Collection<DynamicTest> doubleNegative_negativeSign() {
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            double number = -m_random.nextDouble() * Double.MAX_VALUE;
            tests.add(DynamicTest.dynamicTest("test_" + i, () -> assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(number))));
        }

        return tests;
    }

    @Test
    public void doublePositiveInfinity_zeroSign() {
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(Double.POSITIVE_INFINITY));
    }

    @Test
    public void doubleNegativeInfinity_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void doubleNaN_zeroSign() {
        assertThrows(AssertionError.class, () -> MathExt.getNegativeSign(Double.NaN));
    }

    @Test
    public void doubleMaxPositive_zeroSign() {
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(Double.MAX_VALUE));
    }

    @Test
    public void doubleMinPositive_zeroSign() {
        assertEquals(ZERO_SIGN, MathExt.getNegativeSign(Double.MIN_VALUE));
    }

    @Test
    public void doubleMaxNegative_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(-Double.MAX_VALUE));
    }

    @Test
    public void doubleMinNegative_negativeSign() {
        assertEquals(NEGATIVE_SIGN, MathExt.getNegativeSign(-Double.MIN_VALUE));
    }
}
