/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fun3kochfractalfx.FUN3KochFractalFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import timeutil.TimeStamp;

/**
 *
 * @author Nico Kuijpers
 * Modified for FUN3 by Gertjan Schouten
 */
public class KochManager {
    int threadsFinished =0;
    private ArrayList<Edge> edges;
    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;
    public ExecutorService pool;


    FractalRunnable generatedBottomEdges;
    FractalRunnable generatedRightEdges;
    FractalRunnable generatedLeftEdges;

    public KochManager(FUN3KochFractalFX application) {
        this.edges = new ArrayList<Edge>();
        this.application = application;
        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();
        pool = Executors.newFixedThreadPool(3);
    }

    public  void changeLevel(int nxt)  {
        threadsFinished=0;
        edges.clear();
        clear();
        tsCalc.init();
        tsCalc.setBegin("Begin calculating");
        generatedBottomEdges =new FractalRunnable(nxt,DIRECTION.Bottom);
        generatedRightEdges =new FractalRunnable(nxt,DIRECTION.Right);
        generatedLeftEdges =new FractalRunnable(nxt,DIRECTION.Left);
        updateUi();
      Thread  t = new Thread(() ->{
          pool.submit(generatedBottomEdges);
          pool.submit(generatedRightEdges);
          pool.submit(generatedLeftEdges);
          try {
              edges.addAll(generatedBottomEdges.get());
              edges.addAll(generatedRightEdges.get());
              edges.addAll(generatedLeftEdges.get());
          }
          catch(Exception e){}
            Platform.runLater(()->{
                tsCalc.setEnd("End calculating");
                application.setTextNrEdges("" + edges.size());
                application.setTextCalc(tsCalc.toString());
                drawEdges();
            });
        });

      t.start();

    }
    public void update()
    {
    }
    public void updateUi()
    {
        String totalWork = Integer.toString(generatedBottomEdges.kochFractal.getNrOfEdges()/3);
        application.bottomProgress.progressProperty().bind(generatedBottomEdges.progressProperty());
        application.leftProgress.progressProperty().bind(generatedLeftEdges.progressProperty());
        application.rightProgress.progressProperty().bind(generatedRightEdges.progressProperty());
        application.bottomProgressPercentage.textProperty().bind(generatedBottomEdges.workDoneProperty().asString());
        application.leftProgressPercentage.textProperty().bind(generatedLeftEdges.workDoneProperty().asString());
        application.rightProgressPercentage.textProperty().bind(generatedRightEdges.workDoneProperty().asString());
        application.bottomProgressTotal.setText(totalWork);
        application.rightProgressTotal.setText(totalWork);
        application.leftProgressTotal.setText(totalWork);
    }
    public void drawEdges() {
        tsDraw.init();
        tsDraw.setBegin("Begin drawing");
        application.clearKochPanel();
        synchronized (edges) {
            for (Edge e : edges) {
                application.drawEdge(e);



        edges = new ArrayList<Edge>();
            }
            tsDraw.setEnd("End drawing");
        application.setTextDraw(tsDraw.toString());
        }
    }

    public void clear()
    {

    }

}
