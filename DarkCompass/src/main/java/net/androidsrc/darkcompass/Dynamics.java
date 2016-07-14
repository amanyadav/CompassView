package net.androidsrc.darkcompass;

public class Dynamics {

    /**
     * Used to compare floats, if the difference is smaller than this, they are
     * considered equal
     */
    private static final float TOLERANCE = 0.01f;

    /**
     * The position the dynamics should to be at
     */
    private float targetAngle;

    /**
     * The current position of the dynamics
     */
    private float currentAngle;

    /**
     * The current velocity of the dynamics
     */
    private float velocity;

    /**
     * The time the last update happened
     */
    private long lastTime;

    /**
     * The amount of springiness that the dynamics has
     */
    private float springiness;

    /**
     * The damping that the dynamics has
     */
    private float damping;

    public Dynamics(float springiness, float dampingRatio) {
        this.springiness = springiness;
        this.damping = (float) (dampingRatio * 2f * Math.sqrt(springiness));
    }

    public void setCurrentAngle(float currentAngle, long now) {
        this.currentAngle = currentAngle;
        lastTime = now;
    }

    public void setVelocity(float velocity, long now) {
        this.velocity = velocity;
        lastTime = now;
    }

    public void setTargetAngle(float targetAngle, long now) {
        this.targetAngle = targetAngle;
        lastTime = now;
    }

    public void update(long now) {
        //time unit
        float dt = Math.min(now - lastTime, 50) / 1000f;
        //distance
        float dx = currentAngle - targetAngle;
        //acceleration
        float acceleration = -springiness * dx - damping * velocity;

        velocity += acceleration * dt;
        currentAngle += velocity * dt;

        lastTime = now;
    }

    public boolean isAtRest() {
        final boolean standingStill = Math.abs(velocity) < TOLERANCE;
        final boolean isAtTarget = Math.abs(targetAngle - currentAngle) < TOLERANCE;
        return standingStill && isAtTarget;
    }

    public float getCurrentAngle() {
        return currentAngle;
    }

    public float getTargetAngle() {
        return targetAngle;
    }

    public float getVelocity() {
        return velocity;
    }

}