/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents.AnimationPanel;

import java.awt.Image;
import java.util.ArrayList;

public class Animation {
    private final ArrayList scenes = new ArrayList();
    private int sceneIndex;
    private long movieTime;
    private long totalTime = 0L;

    public Animation() {
        this.start();
    }

    public synchronized void addScene(Image i, long t) {
        this.totalTime += t;
        this.scenes.add(new Scene(i, this.totalTime));
    }

    private void start() {
        this.movieTime = 0L;
        this.sceneIndex = 0;
    }

    public synchronized void update(long timePassed) {
        if (this.scenes.size() > 1) {
            this.movieTime += timePassed;
            if (this.movieTime >= this.totalTime) {
                this.movieTime = 0L;
                this.sceneIndex = 0;
            }
            while (this.movieTime > this.getScene((int)this.sceneIndex).endTime) {
                ++this.sceneIndex;
            }
        }
    }

    public synchronized Image getImage() {
        if (this.scenes.isEmpty()) {
            return null;
        }
        return this.getScene((int)this.sceneIndex).pic;
    }

    public synchronized void replaceScenes(Image i, long t) {
        this.scenes.clear();
        this.totalTime = t;
        this.scenes.add(new Scene(i, this.totalTime));
    }

    private Scene getScene(int x) {
        return (Scene)this.scenes.get(x);
    }

    private class Scene {
        Image pic;
        long endTime;

        public Scene(Image i, long et) {
            this.pic = i;
            this.endTime = et;
        }
    }
}

