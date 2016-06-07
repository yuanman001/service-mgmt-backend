package com.ai.paas.ipaas.ccs.constants;

public class ConfigCenterDubboConstants {
    public enum ZKTypeCode {
        INNER(1), CUSTOM(2);

        int flag;

        ZKTypeCode(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return flag;
        }
        
        //测试
    }

    public enum PathType {
        READONLY("readOnly path"), WRITABLE("writable path");

        private String description;

        PathType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public static String convertPath(String userId, PathType pathType, String path) {
            switch (pathType) {
                case READONLY:
                    return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + "/" + userId + ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_HAS_READ_PREFIX + path;
                case WRITABLE:
                    return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + "/" + userId + ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_HAS_WRITABLE_PREFIX + path;
                default:
            }
            return null;
        }
        
        public static String convertPath(String userId, PathType pathType) {
            switch (pathType) {
                case READONLY:
                    return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + "/" + userId + ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_HAS_READ_PREFIX;
                case WRITABLE:
                    return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + "/" + userId + ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_HAS_WRITABLE_PREFIX;
                default:
            }
            return null;
        }

    }


}
