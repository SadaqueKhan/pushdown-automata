package app.view;

import java.util.List;
import java.util.Random;

public class RandomLayout {

    Graph graph;

    Random rnd = new Random();

    public RandomLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<State> states = graph.getModel().getAllCells();

        for (State state : states) {

            double x = rnd.nextDouble() * 500;
            double y = rnd.nextDouble() * 500;

            state.relocate(x, y);

        }

    }

}