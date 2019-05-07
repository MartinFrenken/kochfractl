package calculate;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class FractalRunnable extends Task<List<Edge>>{
    int level ;


    boolean isDone =false;
    KochFractal kochFractal;
    KochManager manager;
    DIRECTION direction;
    public FractalRunnable(int level,DIRECTION direction)
    {
        this.manager=manager;
        isDone=false;
        this.level=level;
        this.kochFractal= new KochFractal(this);
        this.direction=direction;
        kochFractal.setLevel(level);
    }
    public List<Edge> call()
    {

        if(direction==DIRECTION.Left) {
            kochFractal.generateLeftEdge();
            return kochFractal.generatedEdges;
        }
        if(direction==DIRECTION.Right) {
            kochFractal.generateRightEdge();
            return kochFractal.generatedEdges;
        }
        if(direction==DIRECTION.Bottom) {
            kochFractal.generateBottomEdge();
            return kochFractal.generatedEdges;
        }
        return null;
    }
    public KochFractal getKochFractal() {
        return kochFractal;
    }

   public void update(double x,double y)
   {
        updateProgress(x, y);
    }
}
