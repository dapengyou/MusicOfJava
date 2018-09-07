package com.test.musicofjava.enums;

public enum PlayModeEnum {
    LOOP(0, "列表循环"),//列表循环
    SINGLE(1, "单曲循环"),//单曲循环
    SHUFFLE(2, "随机播放");//随机播放

    private String value;
    private int code;

    PlayModeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static PlayModeEnum valueOf(int value) {
        switch (value) {
            case 2:
                return SHUFFLE;
            case 1:
                return SINGLE;
            case 0:
            default:
                return LOOP;
        }
    }

    public int code(){
        return code;
    }
    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }


    public static String getValue(int code) {
        for (PlayModeEnum state : values()) {
            if (state.getCode() == code) return state.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
