package ru.sbrf.bh.bfs.ufs.parser;

import ru.sbrf.bh.bfs.generator.enums.ArtifactFields;
import ru.sbrf.bh.bfs.generator.parser.Parser;
import ru.sbrf.bh.bfs.model.Adapter;
import ru.sbrf.bh.bfs.grammar.BfsParser.*;

import java.util.List;

/**
 * Created by sbt-barsukov-sv on 14.06.2018.
 */
public class MavenDependencyParser extends Parser<GroupContext,List<Adapter>> {

    public void parse(String key, GroupContext gc, List<Adapter> adapters) {
        if (key.equals(gc.ID().getText())) {
            for (GroupContext igc : gc.group()) {
                String groupId = null, artifactId = null, version = null;
                for (PropertyContext pc : igc.property()) {
                    String text = trm(pc.STRING().getText());
                    String name = pc.ID().getText();

                    if (ArtifactFields.GROUP_ID.getField().equals(name)) groupId = text;
                    if (ArtifactFields.ARTIFACT_ID.getField().equals(name)) artifactId = text;
                    if (ArtifactFields.VERSION.getField().equals(name)) version = text;
                }
                if (groupId != null && artifactId != null && version != null) {
                    adapters.add(new Adapter(groupId, artifactId, version));
                }
            }
        }
    }
}
