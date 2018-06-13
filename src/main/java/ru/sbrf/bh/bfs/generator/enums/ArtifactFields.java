package ru.sbrf.bh.bfs.generator.enums;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public enum ArtifactFields {
    GROUP_ID ("groupId"),
    ARTIFACT_ID ("artifactId"),
    VERSION ("version");

    private String field;
    ArtifactFields(String field) {
        this.field=field;
    }

    public String getField() {
        return field;
    }
}
