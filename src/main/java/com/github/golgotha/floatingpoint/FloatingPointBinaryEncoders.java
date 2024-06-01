package com.github.golgotha.floatingpoint;

/**
 * Serves as a facade for implementations of the {@link FloatingPointBinaryEncoder} interface.
 * This class provides a simplified interface for clients to encode floating-point number string to their binary-encoded
 * representation based of IEEE-754 standard.
 *
 * <p>Usage example:
 *  <pre>{@code
 *  FloatingPointBinaryEncoder encoder = FloatingPointBinaryEncoders.binary32();
 *  String binary32Encoded = FloatingPointBinaryEncoders.binary32().encode("123.32");
 *  String binary64Encoded = FloatingPointBinaryEncoders.binary64().encode("123.32");
 *
 *  System.out.println(binary32Encoded);
 *  System.out.println(binary64Encoded);
 *  }</pre>
 *
 */
public class FloatingPointBinaryEncoders {

    public static FloatingPointBinaryEncoder binary32() {
        return new Binary32FloatingPointBinaryEncoder();
    }

    public static FloatingPointBinaryEncoder binary64() {
        return new Binary64FloatingPointBinaryEncoder();
    }

    private abstract static class AbstractFloatingPointBinaryEncoder implements FloatingPointBinaryEncoder {

        private final int mantissaSize;

        private AbstractFloatingPointBinaryEncoder(int mantissaSize) {
            this.mantissaSize = mantissaSize;
        }

        @Override
        public String encode(String decimal) {
            String sign = decimal.startsWith("-") ? "1" : "0";
            int index = decimal.indexOf(".");
            String integerPart = decimal.substring(0, index);
            String fractionalPart = decimal.substring(index + 1);

            String binaryIntComponent = intToBinary(integerPart);
            String binaryFractionalComponent = fractionalToBinary(fractionalPart);

            String mantissa = binaryIntComponent + binaryFractionalComponent;
            int exponent = binaryIntComponent.length() - 1;
            int adjustedExponent = adjustExponent(exponent);
            String adjustedExponentBinary = intToBinary(adjustedExponent);
            String adjustedMantissa = adjustMantissa(mantissa);

            return sign + adjustedExponentBinary + adjustedMantissa;
        }

        protected abstract int adjustExponent(int exponent);

        private String adjustMantissa(String mantissa) {
            String meaningfulBits = mantissa.substring(1);
            if (meaningfulBits.length() < mantissaSize) {
                // adding necessary zeros to the right
                int numZeros = mantissaSize - meaningfulBits.length();
                StringBuilder sb = new StringBuilder(meaningfulBits);
                for (int i = 0; i < numZeros; i++) {
                    sb.append("0");
                }
                return sb.toString();
            }

            return meaningfulBits.substring(0, mantissaSize);
        }

        private String intToBinary(String value) {
            int intVal = Integer.parseInt(value);
            return intToBinary(intVal);
        }

        private String intToBinary(int value) {
            StringBuilder binaryIntPart = new StringBuilder();
            while (value > 0) {
                int reminder = value % 2;
                binaryIntPart.append(reminder);
                value /= 2;
            }
            return binaryIntPart.reverse().toString();
        }

        private String fractionalToBinary(String fractional) {
            long longFractional = Long.parseLong(fractional);
            int precision = fractional.length();

            StringBuilder binary = new StringBuilder();
            precision = (int) Math.pow(10, precision);
            int maxIterations = mantissaSize;
            while (maxIterations >= 0 && longFractional != precision) {
                longFractional = longFractional * 2;

                binary.append(longFractional / precision > 0 ? "1" : "0");

                if (longFractional - precision > 0) {
                    longFractional = longFractional - precision;
                }

                maxIterations--;
            }
            return binary.toString();
        }

    }


    private static class Binary32FloatingPointBinaryEncoder extends AbstractFloatingPointBinaryEncoder {

        private Binary32FloatingPointBinaryEncoder() {
            super(23);
        }

        protected int adjustExponent(int exponent) {
            return exponent + 127;
        }
    }

    private static class Binary64FloatingPointBinaryEncoder extends AbstractFloatingPointBinaryEncoder {

        private Binary64FloatingPointBinaryEncoder() {
            super(53);
        }

        @Override
        protected int adjustExponent(int exponent) {
            return exponent + 1023;
        }
    }
}
