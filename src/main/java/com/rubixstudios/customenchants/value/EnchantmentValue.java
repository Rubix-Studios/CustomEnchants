package com.rubixstudios.customenchants.value;

public class EnchantmentValue {

    public final static IEnchantmentUsageValues usageValues = new IEnchantmentUsageValues() {

        private final String key = "usages";
        private final String maxUsesKey = "maxuses";
        private final int defaultMaxUsages = 16;

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getMaxKey() {
            return maxUsesKey;
        }

        @Override
        public int getDefaultMaxUsages() {
            return defaultMaxUsages;
        }

    };

    public final static IEnchantmentRepairValues repairValues = new IEnchantmentRepairValues() {

        public final String key = "repairs";

        public final String maxRepairKey = "maxrepairs";

        public final int defaultMaxRepairs = 4;

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getMaxKey() {
            return maxRepairKey;
        }

        @Override
        public int getDefaultMaxRepairs() {
            return defaultMaxRepairs;
        }
    };

    public final static String KILLCOUNTKEY = "killcount";

}
