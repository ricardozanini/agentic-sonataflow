package org.acme.agentic.model;

import java.util.Objects;

public class BudgetPoolRequest {

    private double budget;
    private int attempts;
    private long intervalMs;


    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public long getIntervalMs() {
        return intervalMs;
    }

    public void setIntervalMs(long intervalMs) {
        this.intervalMs = intervalMs;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BudgetPoolRequest that = (BudgetPoolRequest) o;
        return Double.compare(budget, that.budget) == 0 && attempts == that.attempts && intervalMs == that.intervalMs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(budget, attempts, intervalMs);
    }

    @Override
    public String toString() {
        return "BudgetPoolRequest{" +
                "budget=" + budget +
                ", attempts=" + attempts +
                ", intervalMs=" + intervalMs +
                '}';
    }
}
