package com.faforever.client.game.patch;

import com.faforever.client.i18n.I18n;
import com.faforever.client.io.DownloadService;
import com.faforever.client.mod.FeaturedMod;
import com.faforever.client.mod.FeaturedModFile;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.remote.FafService;
import com.faforever.client.task.CompletableTask;
import lombok.extern.slf4j.Slf4j;
import net.brutus5000.deltaforge.client.DeltaforgeClient;
import net.brutus5000.deltaforge.client.model.Repository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class DeltaforgeFeaturedModUpdaterTask extends CompletableTask<PatchResult> {

  private final FafService fafService;
  private final PreferencesService preferencesService;
  private final DownloadService downloadService;
  private final I18n i18n;
  private final DeltaforgeClient deltaforgeClient;

  private FeaturedMod featuredMod;
  private Integer version;

  public DeltaforgeFeaturedModUpdaterTask(FafService fafService, PreferencesService preferencesService, DownloadService downloadService, I18n i18n, DeltaforgeClient deltaforgeClient) {
    super(Priority.HIGH);

    this.fafService = fafService;
    this.preferencesService = preferencesService;
    this.downloadService = downloadService;
    this.i18n = i18n;
    this.deltaforgeClient = deltaforgeClient;
  }

  @Override
  protected PatchResult call() throws Exception {
    String initFileName = "init_" + featuredMod.getTechnicalName() + ".lua";

    updateTitle(i18n.get("updater.taskTitle"));
    updateMessage(i18n.get("updater.readingFileList"));

    Repository repository = deltaforgeClient.loadRepository(featuredMod.getTechnicalName())
      .orElseThrow();

    deltaforgeClient.checkoutTag(repository, version.toString());


    List<FeaturedModFile> featuredModFiles = fafService.getFeaturedModFiles(featuredMod, version).get();
    // TODO: Write lua mappings?


    //TODO: ???
    return null;
  }

  public void setFeaturedMod(FeaturedMod featuredMod) {
    this.featuredMod = featuredMod;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
