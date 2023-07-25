package com.testing.concurrency;

public record ThreadInfo(String treadName, CounterState counterState) {

    public String getThreadName() {
        return treadName;
    }

    public Integer getCount(){
        return counterState.count();
    }

    public Integer getSum(){
        return counterState.sum();
    }

    public Integer getInstruction(){
        return counterState.actualInstruction();
    }

    @Override
    public String toString() {
        return treadName + ": " +
                "c" +counterState.count() +
                ", s" + counterState.sum() +
                ", i" + counterState.actualInstruction();
    }
}
