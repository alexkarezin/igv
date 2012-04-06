/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.broad.igv.feature;

import org.broad.igv.feature.genome.Genome;
import org.broad.igv.feature.tribble.CodecFactory;
import org.broad.igv.track.WindowFunction;
import org.broad.igv.util.TestUtils;
import org.broad.igv.variant.vcf.VCFVariant;
import org.broad.tribble.AbstractFeatureReader;
import org.broad.tribble.CloseableTribbleIterator;
import org.broad.tribble.Feature;
import org.broad.tribble.FeatureCodec;
import org.broadinstitute.sting.utils.codecs.vcf.VCFCodec;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Please create tests!!
 *
 * @author jrobinso
 */
public class FeatureUtilsTest {

    static List<Feature> features;

    public FeatureUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        features = new ArrayList();

        for (int i = 0; i < 1000; i++) {
            features.add(new TestFeature(i, i + 5));
        }

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFeatureLookupInSmallVCF() throws IOException {
        String vcfFile = TestUtils.DATA_DIR + "vcf/example4-last-gsnap-2_fixed.vcf";
        Genome genome = null; // <= Don't do chromosome conversion
        FeatureCodec codec = CodecFactory.getCodec(vcfFile, genome);
        boolean isVCF = codec.getClass().isAssignableFrom(VCFCodec.class);
        AbstractFeatureReader basicReader = AbstractFeatureReader.getFeatureReader(vcfFile, codec, true);
        CloseableTribbleIterator it = basicReader.iterator();
        List<VCFVariant> features = new ArrayList<VCFVariant>();
        while (it.hasNext()) {
            VCFVariant next = (VCFVariant) it.next();
            features.add(next);
        }
        //System.out.println("Now looking up closest to..");
        VCFVariant a_6321739 = (VCFVariant) FeatureUtils.getFeatureClosest(6321739d, features);
        VCFVariant a_6321740 = (VCFVariant) FeatureUtils.getFeatureClosest(6321740d, features);
        //System.out.println(a_6321739);
        //System.out.println(a_6321739);
        assertEquals("variant closest to 6321739 must be found with position=6321739", 6321739, a_6321739.getStart());
        assertEquals("variant closest to 6321740 must be found with position=6321740", 6321740, a_6321740.getStart());


    }

    /**
     * Test of getAllFeaturesAt method, of class FeatureUtils.
     * <p/>
     * Queries the test list for features at position 500.
     */
    @Test
    public void testGetAllFeaturesAt() {
        int position = 500;
        int maxLength = 100;

        List<Feature> result = FeatureUtils.getAllFeaturesAt(position, maxLength, 0, features);
        assertEquals(6, result.size());
        for (Feature f : result) {
            assertTrue(position >= f.getStart() && position <= f.getEnd());
        }


    }

    /**
     * Test of getIndexAfter method, of class FeatureUtils.
     */
    @Test
    public void testGetIndexAfter() {

        List<LocusScore> features = new ArrayList();
        for (int i = 0; i <= 10000; i += 5) {
            int start = i;
            int end = start + 10;
            features.add(new TestFeature(start, end));
        }


        int expResult = 99;
        int result = FeatureUtils.getIndexBefore(499, features);
        assertEquals(expResult, result);

    }

    static class TestFeature implements LocusScore {

        private int start;
        private int end;

        public TestFeature(int start, int end) {
            this.start = start;
            this.end = end;
        }


        public float getScore() {
            return 1.0f;
        }

        public LocusScore copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getValueString(double position, WindowFunction windowFunction) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getChr() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        /**
         * @return the start
         */
        public int getStart() {
            return start;
        }

        /**
         * @param start the start to set
         */
        public void setStart(int start) {
            this.start = start;
        }

        /**
         * @return the end
         */
        public int getEnd() {
            return end;
        }

        /**
         * @param end the end to set
         */
        public void setEnd(int end) {
            this.end = end;
        }
    }
}