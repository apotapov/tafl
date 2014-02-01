package com.pactstudios.games.tafl.core.es;

import com.artemis.World;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.UndoEvent;
import com.roundtriangles.games.zaria.services.resources.LocaleService;


public class HudFactory {

    public static void populateHudRenderingComponent(
            HudRenderingComponent component,
            TaflWorld gameWorld) {

        component.hubStage = gameWorld.stage;

        Skin skin = gameWorld.game.graphicsService.getSkin(Assets.Skin.UI_SKIN);

        createUpperHud(component, skin, gameWorld);
        createLowerHud(component, skin, gameWorld.game.localeService);

        createDialogs(component, gameWorld.world, skin, gameWorld.game.localeService);
    }

    private static void createDialogs(final HudRenderingComponent component, final World world, Skin skin, LocaleService localeService) {

        ChangeListener resumeListener = new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifeCycleEvent lifecycleEvent = SystemEvent.createEvent(LifeCycleEvent.class);
                lifecycleEvent.lifecycle = LifeCycle.PLAY;
                world.postEvent(null, lifecycleEvent);
            }
        };

        ChangeListener restartListener = new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifeCycleEvent lifecycleEvent = SystemEvent.createEvent(LifeCycleEvent.class);
                lifecycleEvent.lifecycle = LifeCycle.RESTART;
                world.postEvent(null, lifecycleEvent);
            }
        };

        ChangeListener quitListener = new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifeCycleEvent lifecycleEvent = SystemEvent.createEvent(LifeCycleEvent.class);
                lifecycleEvent.lifecycle = LifeCycle.QUIT;
                world.postEvent(null, lifecycleEvent);
            }
        };

        createMenuDialog(component, world, skin, localeService, resumeListener, restartListener, quitListener);
        createWinDialog(component, world, skin, localeService, restartListener, quitListener);
        createLossDialog(component, world, skin, localeService, restartListener, quitListener);
        createDrawDialog(component, world, skin, localeService, restartListener, quitListener);
        createPlayerWarningDialog(component, world, skin, localeService, resumeListener);
    }

    private static void createLossDialog(HudRenderingComponent component,
            World world,
            Skin skin,
            LocaleService localeService,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        String text = localeService.get(LocalizedStrings.GameMenu.LOSS_TITLE);
        component.lossDialog = new Dialog(text, skin);
        component.lossDialog.setSkin(skin);

        text = localeService.get(LocalizedStrings.GameMenu.LOSS_TEXT);
        component.lossDialog.add(text);

        text = localeService.get(LocalizedStrings.GameMenu.RESTART_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(restartListener);

        text = localeService.get(LocalizedStrings.GameMenu.MAIN_MENU_BUTTON);
        TextButton quitButton = new TextButton(text, skin);
        quitButton.addListener(quitListener);

        component.lossDialog.button(restartButton);
        component.lossDialog.button(quitButton);
    }

    private static void createDrawDialog(HudRenderingComponent component,
            World world,
            Skin skin,
            LocaleService localeService,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        String text = localeService.get(LocalizedStrings.GameMenu.DRAW_TITLE);
        component.drawDialog = new Dialog(text, skin);
        component.drawDialog.setSkin(skin);

        component.drawText = new Label("", skin);
        component.drawDialog.add(component.drawText);

        text = localeService.get(LocalizedStrings.GameMenu.RESTART_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(restartListener);

        text = localeService.get(LocalizedStrings.GameMenu.MAIN_MENU_BUTTON);
        TextButton quitButton = new TextButton(text, skin);
        quitButton.addListener(quitListener);

        component.drawDialog.button(restartButton);
        component.drawDialog.button(quitButton);
    }

    private static void createWinDialog(HudRenderingComponent component,
            World world,
            Skin skin,
            LocaleService localeService,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        String text = localeService.get(LocalizedStrings.GameMenu.WIN_TITLE);
        component.winDialog = new Dialog(text, skin);
        component.winDialog.setSkin(skin);

        component.winText = new Label("", skin);
        component.winDialog.add(component.winText);

        text = localeService.get(LocalizedStrings.GameMenu.RESTART_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(restartListener);

        text = localeService.get(LocalizedStrings.GameMenu.MAIN_MENU_BUTTON);
        TextButton quitButton = new TextButton(text, skin);
        quitButton.addListener(quitListener);

        component.winDialog.button(restartButton);
        component.winDialog.button(quitButton);
    }

    private static void createPlayerWarningDialog(HudRenderingComponent component,
            World world,
            Skin skin,
            LocaleService localeService,
            ChangeListener resumeListener) {

        String text = localeService.get(LocalizedStrings.GameMenu.PLAYER_WARNING_TITLE);
        component.playerWarningDialog = new Dialog(text, skin);
        component.playerWarningDialog.setSkin(skin);

        component.playerWarningText = new Label("", skin);
        component.playerWarningDialog.add(component.playerWarningText);

        text = localeService.get(LocalizedStrings.GameMenu.OK_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(resumeListener);

        component.playerWarningDialog.button(restartButton);
    }

    private static void createMenuDialog(HudRenderingComponent component,
            World world,
            Skin skin,
            LocaleService localeService,
            ChangeListener resumeListener,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        String text = localeService.get(LocalizedStrings.GameMenu.MENU_TITLE);
        component.menu = new Dialog(text, skin);

        component.menu.setSkin(skin);

        text = localeService.get(LocalizedStrings.GameMenu.MENU_TEXT);
        component.menu.add(text);

        text = localeService.get(LocalizedStrings.GameMenu.RESUME_BUTTON);
        TextButton resumeButton = new TextButton(text, skin);
        resumeButton.addListener(resumeListener);
        component.menu.button(resumeButton);

        text = localeService.get(LocalizedStrings.GameMenu.RESTART_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(restartListener);

        text = localeService.get(LocalizedStrings.GameMenu.MAIN_MENU_BUTTON);
        TextButton quitButton = new TextButton(text, skin);
        quitButton.addListener(quitListener);

        component.menu.button(restartButton);
        component.menu.button(quitButton);
    }

    private static void createUpperHud(HudRenderingComponent component, Skin skin, TaflWorld gameWorld) {

        Table table = new Table(skin);

        createMenu(component, skin, table, gameWorld);

        component.turn = new Label("", skin);
        table.add(component.turn).expandX();

        component.timer = new Label("", skin);
        table.add(component.timer).expandX();

        if (Constants.GameConstants.DEBUG) {
            component.fps = new Label("", skin);
            table.add(component.fps).expandX();
        }

        createUndo(component, skin, table, gameWorld);

        table.right().top().setFillParent(true);
        component.hubStage.addActor(table);
    }

    private static void createLowerHud(HudRenderingComponent component, Skin skin, LocaleService localeService) {
        Table table = new Table();

        //        component.log = new List(new Object[] {}, skin);
        //        table.add(component.log).expandX();
        //        table.row();

        table.right().top().setFillParent(true);
        component.hubStage.addActor(table);
    }


    private static void createUndo(HudRenderingComponent component,
            Skin skin, Table table, TaflWorld gameWorld) {

        final World world = gameWorld.world;

        String text = gameWorld.game.localeService.get(LocalizedStrings.Hud.UNDO_BUTTON);
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                UndoEvent undoEvent = SystemEvent.createEvent(UndoEvent.class);
                world.postEvent(null, undoEvent);
            }
        });
        table.add(button).size(Constants.HudConstants.BUTTON_WIDTH, Constants.HudConstants.BUTTON_HEIGHT).uniform();
    }

    private static void createMenu(HudRenderingComponent component,
            Skin skin, Table table, final TaflWorld gameWorld) {

        final World world = gameWorld.world;

        String text = gameWorld.game.localeService.get(LocalizedStrings.Hud.MENU_BUTTON);
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifeCycleEvent lifecycleEvent = SystemEvent.createEvent(LifeCycleEvent.class);
                lifecycleEvent.lifecycle = LifeCycle.MENU;
                world.postEvent(null, lifecycleEvent);
            }
        });
        table.add(button).size(Constants.HudConstants.BUTTON_WIDTH, Constants.HudConstants.BUTTON_HEIGHT).uniform();
    }
}
