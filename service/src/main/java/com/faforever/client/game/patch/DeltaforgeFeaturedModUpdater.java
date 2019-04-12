package com.faforever.client.game.patch;

import com.faforever.client.SpringProfiles;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.task.TaskService;
import lombok.extern.slf4j.Slf4j;
import net.brutus5000.deltaforge.client.DeltaforgeClient;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Lazy
@Component
@Profile("!" + SpringProfiles.PROFILE_OFFLINE)
@Slf4j
public class DeltaforgeFeaturedModUpdater implements FeaturedModUpdater {

  private final TaskService taskService;
  private final DeltaforgeClient deltaforgeClient;
  private final ApplicationContext applicationContext;


  public DeltaforgeFeaturedModUpdater(TaskService taskService, DeltaforgeClient deltaforgeClient, ApplicationContext applicationContext) {
    this.taskService = taskService;
    this.deltaforgeClient = deltaforgeClient;
    this.applicationContext = applicationContext;
  }

  @Override
  public CompletableFuture<PatchResult> updateMod(FeaturedMod featuredMod, @Nullable Integer version) {
    DeltaforgeFeaturedModUpdaterTask task = applicationContext.getBean(DeltaforgeFeaturedModUpdaterTask.class);
    task.setVersion(version);
    task.setFeaturedMod(featuredMod);

    return taskService.submitTask(task).getFuture();
  }

  @Override
  public boolean canUpdate(FeaturedMod featuredMod) {
    try {
      return deltaforgeClient.loadRepository(featuredMod.getTechnicalName()).isPresent();
    } catch (Exception e) {
      log.warn("Loading Deltaforge repository failed", e);
      return false;
    }
  }
}
