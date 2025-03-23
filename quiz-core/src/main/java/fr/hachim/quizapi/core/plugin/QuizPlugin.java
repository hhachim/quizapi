package fr.hachim.quizapi.core.plugin;

public interface QuizPlugin {
    String getId();
    String getName();
    String getVersion();
    String getDescription();
    
    void initialize();
    void shutdown();
    boolean isEnabled();
    void setEnabled(boolean enabled);
}