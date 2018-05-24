package ru.sbrf.bh.bfs;

import ru.sbrf.bh.bfs.grammar.bfsBaseListener;
import ru.sbrf.bh.bfs.grammar.bfsParser;

public class HelloWalker extends bfsBaseListener {
    public void enterR(HelloParser.RContext ctx) {
        for (bfsParser.GroupContext gc : ctx.group()) {
            System.out.println(gc.ID().getText());
            for (bfsParser.PropertyContext pc : gc.property()) {
                System.out.println(pc.ID().getText() + " = " + pc.STRING().getText());
            }
        }
    }

    public void exitR(HelloParser.RContext ctx) {
        System.out.println("Exiting R");
    }
}