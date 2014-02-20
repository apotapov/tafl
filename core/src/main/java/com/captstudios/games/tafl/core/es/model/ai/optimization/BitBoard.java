package com.captstudios.games.tafl.core.es.model.ai.optimization;

import java.util.Arrays;

/**
 * Game bit board based on org.apache.lucene.util.BitBoard version 4.5.0
 */

public class BitBoard implements Cloneable {

    /**  A variety of high efficiency bit twiddling routines.
     */
    public static final class BitUtil {

        private static final byte[] BYTE_COUNTS = {  // table of bits/byte
            0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4,
            1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
            1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
            2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
            1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
            2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
            2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
            3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
            1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
            2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
            2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
            3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
            2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
            3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
            3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
            4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8
        };

        // The General Idea: instead of having an array per byte that has
        // the offsets of the next set bit, that array could be
        // packed inside a 32 bit integer (8 4 bit numbers).  That
        // should be faster than accessing an array for each index, and
        // the total array size is kept smaller (256*sizeof(int))=1K
        /***** the python code that generated bitlist
      def bits2int(val):
      arr=0
      for shift in range(8,0,-1):
        if val & 0x80:
          arr = (arr << 4) | shift
        val = val << 1
      return arr

      def int_table():
        tbl = [ hex(bits2int(val)).strip('L') for val in range(256) ]
        return ','.join(tbl)
         ******/
        private static final int[] BIT_LISTS = {
            0x0, 0x1, 0x2, 0x21, 0x3, 0x31, 0x32, 0x321, 0x4, 0x41, 0x42, 0x421, 0x43,
            0x431, 0x432, 0x4321, 0x5, 0x51, 0x52, 0x521, 0x53, 0x531, 0x532, 0x5321,
            0x54, 0x541, 0x542, 0x5421, 0x543, 0x5431, 0x5432, 0x54321, 0x6, 0x61, 0x62,
            0x621, 0x63, 0x631, 0x632, 0x6321, 0x64, 0x641, 0x642, 0x6421, 0x643,
            0x6431, 0x6432, 0x64321, 0x65, 0x651, 0x652, 0x6521, 0x653, 0x6531, 0x6532,
            0x65321, 0x654, 0x6541, 0x6542, 0x65421, 0x6543, 0x65431, 0x65432, 0x654321,
            0x7, 0x71, 0x72, 0x721, 0x73, 0x731, 0x732, 0x7321, 0x74, 0x741, 0x742,
            0x7421, 0x743, 0x7431, 0x7432, 0x74321, 0x75, 0x751, 0x752, 0x7521, 0x753,
            0x7531, 0x7532, 0x75321, 0x754, 0x7541, 0x7542, 0x75421, 0x7543, 0x75431,
            0x75432, 0x754321, 0x76, 0x761, 0x762, 0x7621, 0x763, 0x7631, 0x7632,
            0x76321, 0x764, 0x7641, 0x7642, 0x76421, 0x7643, 0x76431, 0x76432, 0x764321,
            0x765, 0x7651, 0x7652, 0x76521, 0x7653, 0x76531, 0x76532, 0x765321, 0x7654,
            0x76541, 0x76542, 0x765421, 0x76543, 0x765431, 0x765432, 0x7654321, 0x8,
            0x81, 0x82, 0x821, 0x83, 0x831, 0x832, 0x8321, 0x84, 0x841, 0x842, 0x8421,
            0x843, 0x8431, 0x8432, 0x84321, 0x85, 0x851, 0x852, 0x8521, 0x853, 0x8531,
            0x8532, 0x85321, 0x854, 0x8541, 0x8542, 0x85421, 0x8543, 0x85431, 0x85432,
            0x854321, 0x86, 0x861, 0x862, 0x8621, 0x863, 0x8631, 0x8632, 0x86321, 0x864,
            0x8641, 0x8642, 0x86421, 0x8643, 0x86431, 0x86432, 0x864321, 0x865, 0x8651,
            0x8652, 0x86521, 0x8653, 0x86531, 0x86532, 0x865321, 0x8654, 0x86541,
            0x86542, 0x865421, 0x86543, 0x865431, 0x865432, 0x8654321, 0x87, 0x871,
            0x872, 0x8721, 0x873, 0x8731, 0x8732, 0x87321, 0x874, 0x8741, 0x8742,
            0x87421, 0x8743, 0x87431, 0x87432, 0x874321, 0x875, 0x8751, 0x8752, 0x87521,
            0x8753, 0x87531, 0x87532, 0x875321, 0x8754, 0x87541, 0x87542, 0x875421,
            0x87543, 0x875431, 0x875432, 0x8754321, 0x876, 0x8761, 0x8762, 0x87621,
            0x8763, 0x87631, 0x87632, 0x876321, 0x8764, 0x87641, 0x87642, 0x876421,
            0x87643, 0x876431, 0x876432, 0x8764321, 0x8765, 0x87651, 0x87652, 0x876521,
            0x87653, 0x876531, 0x876532, 0x8765321, 0x87654, 0x876541, 0x876542,
            0x8765421, 0x876543, 0x8765431, 0x8765432, 0x87654321
        };

        private BitUtil() {} // no instance

        /** Return the number of bits sets in b. */
        public static int bitCount(byte b) {
            return BYTE_COUNTS[b & 0xFF];
        }

        /** Return the list of bits which are set in b encoded as followed:
         * <code>(i >>> (4 * n)) & 0x0F</code> is the offset of the n-th set bit of
         * the given byte plus one, or 0 if there are n or less bits set in the given
         * byte. For example <code>bitList(12)</code> returns 0x43:<ul>
         * <li><code>0x43 & 0x0F</code> is 3, meaning the the first bit set is at offset 3-1 = 2,</li>
         * <li><code>(0x43 >>> 4) & 0x0F</code> is 4, meaning there is a second bit set at offset 4-1=3,</li>
         * <li><code>(0x43 >>> 8) & 0x0F</code> is 0, meaning there is no more bit set in this byte.</li>
         * </ul>*/
        public static int bitList(byte b) {
            return BIT_LISTS[b & 0xFF];
        }

        // The pop methods used to rely on bit-manipulation tricks for speed but it
        // turns out that it is faster to use the Long.bitCount method (which is an
        // intrinsic since Java 6u18) in a naive loop, see LUCENE-2221

        /** Returns the number of set bits in an array of longs. */
        public static int pop_array(long[] arr, int wordOffset, int numWords) {
            int popCount = 0;
            for (int i = wordOffset, end = wordOffset + numWords; i < end; ++i) {
                popCount += Long.bitCount(arr[i]);
            }
            return popCount;
        }

        /** Returns the popcount or cardinality of the two sets after an intersection.
         *  Neither array is modified. */
        public static int pop_intersect(long[] arr1, long[] arr2, int wordOffset, int numWords) {
            int popCount = 0;
            for (int i = wordOffset, end = wordOffset + numWords; i < end; ++i) {
                popCount += Long.bitCount(arr1[i] & arr2[i]);
            }
            return popCount;
        }

        /** Returns the popcount or cardinality of the union of two sets.
         *  Neither array is modified. */
        public static int pop_union(long[] arr1, long[] arr2, int wordOffset, int numWords) {
            int popCount = 0;
            for (int i = wordOffset, end = wordOffset + numWords; i < end; ++i) {
                popCount += Long.bitCount(arr1[i] | arr2[i]);
            }
            return popCount;
        }

        /** Returns the popcount or cardinality of A & ~B.
         *  Neither array is modified. */
        public static int pop_andnot(long[] arr1, long[] arr2, int wordOffset, int numWords) {
            int popCount = 0;
            for (int i = wordOffset, end = wordOffset + numWords; i < end; ++i) {
                popCount += Long.bitCount(arr1[i] & ~arr2[i]);
            }
            return popCount;
        }

        /** Returns the popcount or cardinality of A ^ B
         * Neither array is modified. */
        public static int pop_xor(long[] arr1, long[] arr2, int wordOffset, int numWords) {
            int popCount = 0;
            for (int i = wordOffset, end = wordOffset + numWords; i < end; ++i) {
                popCount += Long.bitCount(arr1[i] ^ arr2[i]);
            }
            return popCount;
        }

        /** returns the next highest power of two, or the current value if it's already a power of two or zero*/
        public static int nextHighestPowerOfTwo(int v) {
            v--;
            v |= v >> 1;
            v |= v >> 2;
            v |= v >> 4;
            v |= v >> 8;
            v |= v >> 16;
            v++;
            return v;
        }

    }


    protected long[] bits;
    protected int wlen; // number of words (elements) used in the array

    /**
     * For serialization only.
     */
    @SuppressWarnings("unused")
    private BitBoard() {
    }

    /**
     * Constructs an BitBoard large enough to hold <code>numBits</code>.
     */
    public BitBoard(int numBits) {
        bits = new long[bits2words(numBits)];
        wlen = bits.length;
    }

    /**
     * Returns the current capacity in bits (1 greater than the index of the
     * last bit)
     */
    public int capacity() {
        return bits.length << 6;
    }

    /**
     * Returns the current capacity of this set. Included for compatibility.
     * This is *not* equal to {@link #cardinality}
     */
    public int size() {
        return bits.length << 6;
    }

    public int length() {
        return bits.length << 6;
    }

    /** Returns true if there are no set bits */
    public boolean isEmpty() {
        return cardinality() == 0;
    }

    /** Expert: returns the long[] storing the bits */
    public long[] getBits() {
        return bits;
    }

    /** Expert: sets a new long[] to use as the bit storage */
    public void setBits(long[] bits) {
        this.bits = bits;
    }

    /** Expert: gets the number of longs in the array that are in use */
    public int getNumWords() {
        return wlen;
    }

    /** Expert: sets the number of longs in the array that are in use */
    public void setNumWords(int nWords) {
        this.wlen = nWords;
    }

    /** Returns true or false for the specified bit index. */
    public boolean get(int index) {
        int i = index >> 6; // div 64
        // signed shift will keep a negative index and force an
        // array-index-out-of-bounds-exception, removing the need for an
        // explicit check.
        int bit = index & 0x3f; // mod 64
        long bitmask = 1L << bit;
        return (bits[i] & bitmask) != 0;
    }

    /*
     * // alternate implementation of get() public boolean get1(int index) { int
     * i = index >> 6; // div 64 int bit = index & 0x3f; // mod 64 return
     * ((bits[i]>>>bit) & 0x01) != 0; // this does a long shift and a bittest
     * (on x86) vs // a long shift, and a long AND, (the test for zero is prob a
     * no-op) // testing on a P4 indicates this is slower than (bits[i] &
     * bitmask) != 0; }
     */

    /**
     * returns 1 if the bit is set, 0 if not. The index should be less than the
     * BitBoard size
     */
    public int getBit(int index) {
        int i = index >> 6; // div 64
        int bit = index & 0x3f; // mod 64
        return ((int) (bits[i] >>> bit)) & 0x01;
    }

    /*
     * public boolean get2(int index) { int word = index >> 6; // div 64 int bit
     * = index & 0x0000003f; // mod 64 return (bits[word] << bit) < 0; // hmmm,
     * this would work if bit order were reversed // we could right shift and
     * check for parity bit, if it was available to us. }
     */

    /** sets a bit, expanding the set size if necessary */
    public void set(int index) {
        int wordNum = index >> 6; // div 64
        int bit = index & 0x3f; // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] |= bitmask;
    }


    /**
     * Sets a range of bits, expanding the set size if necessary
     * 
     * @param startIndex
     *            lower index
     * @param endIndex
     *            one-past the last bit to set
     */
    public void set(int startIndex, int endIndex) {
        if (endIndex <= startIndex) {
            return;
        }

        int startWord = startIndex >> 6;

        // since endIndex is one past the end, this is index of the last
        // word to be changed.
        int endWord = (endIndex - 1) >> 6;

        long startmask = -1L << startIndex;
        long endmask = -1L >>> -endIndex; // 64-(endIndex&0x3f) is the same as
        // -endIndex due to wrap

        if (startWord == endWord) {
            bits[startWord] |= (startmask & endmask);
            return;
        }

        bits[startWord] |= startmask;
        Arrays.fill(bits, startWord + 1, endWord, -1L);
        bits[endWord] |= endmask;
    }


    /**
     * clears a bit. The index should be less than the BitBoard size.
     */
    public void clear(int index) {
        int wordNum = index >> 6;
        int bit = index & 0x03f;
        long bitmask = 1L << bit;
        bits[wordNum] &= ~bitmask;
        // hmmm, it takes one more instruction to clear than it does to set...
        // any
        // way to work around this? If there were only 63 bits per word, we
        // could
        // use a right shift of 10111111...111 in binary to position the 0 in
        // the
        // correct place (using sign extension).
        // Could also use Long.rotateRight() or rotateLeft() *if* they were
        // converted
        // by the JVM into a native instruction.
        // bits[word] &= Long.rotateLeft(0xfffffffe,bit);
    }

    /**
     * Clears a range of bits. Clearing past the end does not change the size of
     * the set.
     * 
     * @param startIndex
     *            lower index
     * @param endIndex
     *            one-past the last bit to clear
     */
    public void clear(int startIndex, int endIndex) {
        if (endIndex <= startIndex) {
            return;
        }

        int startWord = (startIndex >> 6);
        if (startWord >= wlen) {
            return;
        }

        // since endIndex is one past the end, this is index of the last
        // word to be changed.
        int endWord = (endIndex - 1) >> 6;

        long startmask = -1L << startIndex;
        long endmask = -1L >>> -endIndex; // 64-(endIndex&0x3f) is the same as
        // -endIndex due to wrap

        // invert masks since we are clearing
        startmask = ~startmask;
        endmask = ~endmask;

        if (startWord == endWord) {
            bits[startWord] &= (startmask | endmask);
            return;
        }

        bits[startWord] &= startmask;

        int middle = Math.min(wlen, endWord);
        Arrays.fill(bits, startWord + 1, middle, 0L);
        if (endWord < wlen) {
            bits[endWord] &= endmask;
        }
    }

    public BitBoard clear() {
        Arrays.fill(bits, 0);
        return this;
    }

    /**
     * Sets a bit and returns the previous value. The index should be less than
     * the BitBoard size.
     */
    public boolean getAndSet(int index) {
        int wordNum = index >> 6; // div 64
        int bit = index & 0x3f; // mod 64
        long bitmask = 1L << bit;
        boolean val = (bits[wordNum] & bitmask) != 0;
        bits[wordNum] |= bitmask;
        return val;
    }

    /**
     * flips a bit. The index should be less than the BitBoard size.
     */
    public void flip(int index) {
        int wordNum = index >> 6; // div 64
        int bit = index & 0x3f; // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] ^= bitmask;
    }

    /**
     * flips a bit and returns the resulting bit value. The index should be less
     * than the BitBoard size.
     */
    public boolean flipAndGet(int index) {
        // assert index >= 0 && index < numBits;
        int wordNum = index >> 6; // div 64
        int bit = index & 0x3f; // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] ^= bitmask;
        return (bits[wordNum] & bitmask) != 0;
    }

    /**
     * Flips a range of bits, expanding the set size if necessary
     * 
     * @param startIndex
     *            lower index
     * @param endIndex
     *            one-past the last bit to flip
     */
    public void flip(int startIndex, int endIndex) {
        if (endIndex <= startIndex) {
            return;
        }
        int startWord = startIndex >> 6;

        // since endIndex is one past the end, this is index of the last
        // word to be changed.
        int endWord = (endIndex - 1) >> 6;

        /***
         * Grrr, java shifting wraps around so -1L>>>64 == -1 for that reason,
         * make sure not to use endmask if the bits to flip will be zero in the
         * last word (redefine endWord to be the last changed...) long startmask
         * = -1L << (startIndex & 0x3f); // example: 11111...111000 long endmask
         * = -1L >>> (64-(endIndex & 0x3f)); // example: 00111...111111
         ***/

        long startmask = -1L << startIndex;
        long endmask = -1L >>> -endIndex; // 64-(endIndex&0x3f) is the same as
        // -endIndex due to wrap

        if (startWord == endWord) {
            bits[startWord] ^= (startmask & endmask);
            return;
        }

        bits[startWord] ^= startmask;

        for (int i = startWord + 1; i < endWord; i++) {
            bits[i] = ~bits[i];
        }

        bits[endWord] ^= endmask;
    }

    /*
     * public static int pop(long v0, long v1, long v2, long v3) { // derived
     * from pop_array by setting last four elems to 0. // exchanges one pop()
     * call for 10 elementary operations // saving about 7 instructions... is
     * there a better way? long twosA=v0 & v1; long ones=v0^v1;
     * 
     * long u2=ones^v2; long twosB =(ones&v2)|(u2&v3); ones=u2^v3;
     * 
     * long fours=(twosA&twosB); long twos=twosA^twosB;
     * 
     * return (pop(fours)<<2) + (pop(twos)<<1) + pop(ones);
     * 
     * }
     */

    /** @return the number of set bits */
    public int cardinality() {
        return BitUtil.pop_array(bits, 0, wlen);
    }

    /**
     * Returns the popcount or cardinality of the intersection of the two sets.
     * Neither set is modified.
     */
    public static int intersectionCount(BitBoard a, BitBoard b) {
        return BitUtil.pop_intersect(a.bits, b.bits, 0,
                Math.min(a.wlen, b.wlen));
    }

    /**
     * Returns the popcount or cardinality of the union of the two sets. Neither
     * set is modified.
     */
    public static long unionCount(BitBoard a, BitBoard b) {
        long tot = BitUtil.pop_union(a.bits, b.bits, 0,
                Math.min(a.wlen, b.wlen));
        if (a.wlen < b.wlen) {
            tot += BitUtil.pop_array(b.bits, a.wlen, b.wlen - a.wlen);
        } else if (a.wlen > b.wlen) {
            tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen - b.wlen);
        }
        return tot;
    }

    /**
     * Returns the popcount or cardinality of "a and not b" or
     * "intersection(a, not(b))". Neither set is modified.
     */
    public static long andNotCount(BitBoard a, BitBoard b) {
        long tot = BitUtil.pop_andnot(a.bits, b.bits, 0,
                Math.min(a.wlen, b.wlen));
        if (a.wlen > b.wlen) {
            tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen - b.wlen);
        }
        return tot;
    }

    /**
     * Returns the popcount or cardinality of the exclusive-or of the two sets.
     * Neither set is modified.
     */
    public static long xorCount(BitBoard a, BitBoard b) {
        long tot = BitUtil.pop_xor(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
        if (a.wlen < b.wlen) {
            tot += BitUtil.pop_array(b.bits, a.wlen, b.wlen - a.wlen);
        } else if (a.wlen > b.wlen) {
            tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen - b.wlen);
        }
        return tot;
    }

    /**
     * Returns the index of the first set bit starting at the index specified.
     * -1 is returned if there are no more set bits.
     */
    public int nextSetBit(int index) {
        int i = index >> 6;
        if (i >= wlen) {
            return -1;
        }
        int subIndex = index & 0x3f; // index within the word
        long word = bits[i] >> subIndex; // skip all the bits to the right of
        // index

        if (word != 0) {
            return (i << 6) + subIndex + Long.numberOfTrailingZeros(word);
        }

        while (++i < wlen) {
            word = bits[i];
            if (word != 0) {
                return (i << 6) + Long.numberOfTrailingZeros(word);
            }
        }

        return -1;
    }

    /**
     * Returns the index of the first set bit starting downwards at the index
     * specified. -1 is returned if there are no more set bits.
     */
    public int prevSetBit(int index) {
        int i = index >> 6;
                final int subIndex;
                long word;
                if (i >= wlen) {
                    i = wlen - 1;
                    if (i < 0) {
                        return -1;
                    }
                    subIndex = 63; // last possible bit
                    word = bits[i];
                } else {
                    if (i < 0) {
                        return -1;
                    }
                    subIndex = index & 0x3f; // index within the word
                    word = (bits[i] << (63 - subIndex)); // skip all the bits to the
                    // left of index
                }

                if (word != 0) {
                    return (i << 6) + subIndex - Long.numberOfLeadingZeros(word); // See
                    // LUCENE-3197
                }

                while (--i >= 0) {
                    word = bits[i];
                    if (word != 0) {
                        return (i << 6) + 63 - Long.numberOfLeadingZeros(word);
                    }
                }

                return -1;
    }

    @Override
    public BitBoard clone() {
        try {
            BitBoard obs = (BitBoard) super.clone();
            obs.bits = obs.bits.clone(); // hopefully an array clone is as
            // fast(er) than arraycopy
            return obs;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /** this = this XOR other */
    public BitBoard xor(BitBoard other) {
        int newLen = Math.max(wlen, other.wlen);

        long[] thisArr = this.bits;
        long[] otherArr = other.bits;
        int pos = Math.min(wlen, other.wlen);
        while (--pos >= 0) {
            thisArr[pos] ^= otherArr[pos];
        }
        if (this.wlen < newLen) {
            System.arraycopy(otherArr, this.wlen, thisArr, this.wlen, newLen
                    - this.wlen);
        }
        this.wlen = newLen;
        return this;
    }

    public BitBoard set(BitBoard other) {
        System.arraycopy(other.bits, 0, bits, 0, bits.length);
        return this;
    }

    // some BitBoard compatability methods

    // ** see {@link intersect} */
    public BitBoard and(BitBoard other) {
        int newLen = Math.min(this.wlen, other.wlen);
        long[] thisArr = this.bits;
        long[] otherArr = other.bits;
        // testing against zero can be more efficient
        int pos = newLen;
        while (--pos >= 0) {
            thisArr[pos] &= otherArr[pos];
        }
        if (this.wlen > newLen) {
            // fill zeros from the new shorter length to the old length
            Arrays.fill(bits, newLen, this.wlen, 0);
        }
        this.wlen = newLen;
        return this;
    }

    // ** see {@link union} */
    public BitBoard or(BitBoard other) {
        int newLen = Math.max(wlen, other.wlen);
        // assert (numBits = Math.max(other.numBits, numBits)) >= 0;

        long[] thisArr = this.bits;
        long[] otherArr = other.bits;
        int pos = Math.min(wlen, other.wlen);
        while (--pos >= 0) {
            thisArr[pos] |= otherArr[pos];
        }
        if (this.wlen < newLen) {
            System.arraycopy(otherArr, this.wlen, thisArr, this.wlen, newLen
                    - this.wlen);
        }
        this.wlen = newLen;
        return this;
    }

    // ** see {@link andNot} */
    public BitBoard andNot(BitBoard other) {
        int idx = Math.min(wlen, other.wlen);
        long[] thisArr = this.bits;
        long[] otherArr = other.bits;
        while (--idx >= 0) {
            thisArr[idx] &= ~otherArr[idx];
        }
        return this;
    }

    /** returns true if the sets have any elements in common */
    public boolean intersects(BitBoard other) {
        int pos = Math.min(this.wlen, other.wlen);
        long[] thisArr = this.bits;
        long[] otherArr = other.bits;
        while (--pos >= 0) {
            if ((thisArr[pos] & otherArr[pos]) != 0) {
                return true;
            }
        }
        return false;
    }

    /** returns the number of 64 bit words it would take to hold numBits */
    public static int bits2words(long numBits) {
        return (int) (((numBits - 1) >>> 6) + 1);
    }

    /** returns true if both sets have the same bits set */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BitBoard)) {
            return false;
        }
        BitBoard a;
        BitBoard b = (BitBoard) o;
        // make a the larger set.
        if (b.wlen > this.wlen) {
            a = b;
            b = this;
        } else {
            a = this;
        }

        // check for any set bits out of the range of b
        for (int i = a.wlen - 1; i >= b.wlen; i--) {
            if (a.bits[i] != 0) {
                return false;
            }
        }

        for (int i = b.wlen - 1; i >= 0; i--) {
            if (a.bits[i] != b.bits[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        // Start with a zero hash and use a mix that results in zero if the
        // input is zero.
        // This effectively truncates trailing zeros without an explicit check.
        long h = 0;
        for (int i = bits.length; --i >= 0;) {
            h ^= bits[i];
            h = (h << 1) | (h >>> 63); // rotate left
        }
        // fold leftmost bits into right and add a constant to prevent
        // empty sets from returning 0, which is too common.
        return (int) ((h >> 32) ^ h) + 0x98761234;
    }

    /**
     * Returns a string representation of this bit set. For every index
     * for which this {@code BitSet} contains a bit in the set
     * state, the decimal representation of that index is included in
     * the result. Such indices are listed in order from lowest to
     * highest, separated by ",&nbsp;" (a comma and a space) and
     * surrounded by braces, resulting in the usual mathematical
     * notation for a set of integers.
     *
     * <p>Example:
     * <pre>
     * BitSet drPepper = new BitSet();</pre>
     * Now {@code drPepper.toString()} returns "{@code {}}".<p>
     * <pre>
     * drPepper.set(2);</pre>
     * Now {@code drPepper.toString()} returns "{@code {2}}".<p>
     * <pre>
     * drPepper.set(4);
     * drPepper.set(10);</pre>
     * Now {@code drPepper.toString()} returns "{@code {2, 4, 10}}".
     *
     * @return a string representation of this bit set
     */
    @Override
    public String toString() {
        int numBits = cardinality();
        StringBuilder b = new StringBuilder(6*numBits + 2);

        int i = nextSetBit(0);
        if (i != -1) {
            b.append(i);
            for (i = nextSetBit(i+1); i >= 0; i = nextSetBit(i+1)) {
                b.append(", ").append(i);
            }
        }

        b.append('\n');
        b.append('\n');

        for (i = 10; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                if (get(i * 11 + j)) {
                    b.append('0');
                } else {
                    b.append('.');
                }
            }
            b.append('\n');
        }

        return b.toString();
    }
}
