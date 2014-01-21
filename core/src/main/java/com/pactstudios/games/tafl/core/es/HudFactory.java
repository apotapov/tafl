package com.pactstudios.games.tafl.core.es;

import com.artemis.World;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.pactstudios.games.tafl.core.consts.Assets;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.consts.LocalizedStrings;
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.pactstudios.games.tafl.core.es.systems.render.hud.HudBackground;
import com.roundtriangles.games.zaria.services.LocaleService;


public class HudFactory {

    public static void populateHudRenderingComponent(
            HudRenderingComponent component,
            TaflWorld gameWorld) {

        LocaleService localeService = gameWorld.game.getLocaleService();

        component.hubStage = gameWorld.stage;

        Skin skin = gameWorld.game.getGraphicsService().getSkin(Assets.Skin.UI_SKIN);

        createUpperHud(component, skin, gameWorld);
        createLowerHud(component, skin, localeService);

        createDialogs(component, gameWorld.world, skin, localeService);
    }

    private static void createDialogs(final HudRenderingComponent component, final World world, Skin skin, LocaleService localeService) {

        ChangeListener restartListener = new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifecycleEvent lifecycleEvent = SystemEvent.createEvent(LifecycleEvent.class);
                lifecycleEvent.lifecycle = Lifecycle.RESTART;
                world.postEvent(null, lifecycleEvent);
            }
        };

        ChangeListener quitListener = new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifecycleEvent lifecycleEvent = SystemEvent.createEvent(LifecycleEvent.class);
                lifecycleEvent.lifecycle = Lifecycle.QUIT;
                world.postEvent(null, lifecycleEvent);
            }
        };

        createMenuDialog(component, world, skin, localeService, restartListener, quitListener);
        createWinDialog(component, world, skin, localeService, restartListener, quitListener);
        createLossDialog(component, world, skin, localeService, restartListener, quitListener);
    }

    private static void createLossDialog(HudRenderingComponent component,
            World world, Skin skin, LocaleService localeService, ChangeListener restartListener,
            ChangeListener quitListener) {

        String text = localeService._(LocalizedStrings.GameMenu.LOSS_TITLE);
        component.lossDialog = new Dialog(text, skin);

        component.lossDialog.setSkin(skin);
        text = localeService._(LocalizedStrings.GameMenu.LOSS_TEXT);
        component.lossDialog.add(text);

        text = localeService._(LocalizedStrings.GameMenu.RESTART_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(restartListener);

        text = localeService._(LocalizedStrings.GameMenu.MAIN_MENU_BUTTON);
        TextButton quitButton = new TextButton(text, skin);
        quitButton.addListener(quitListener);

        component.lossDialog.button(restartButton);
        component.lossDialog.button(quitButton);
    }

    private static void createWinDialog(HudRenderingComponent component,
            World world, Skin skin, LocaleService localeService, ChangeListener restartListener,
            ChangeListener quitListener) {

        String text = localeService._(LocalizedStrings.GameMenu.WIN_TITLE);
        component.winDialog = new Dialog(text, skin);

        component.winDialog.setSkin(skin);

        text = localeService._(LocalizedStrings.GameMenu.WIN_TEXT);
        component.winDialog.add(text);

        text = localeService._(LocalizedStrings.GameMenu.RESTART_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(restartListener);

        text = localeService._(LocalizedStrings.GameMenu.MAIN_MENU_BUTTON);
        TextButton quitButton = new TextButton(text, skin);
        quitButton.addListener(quitListener);

        component.winDialog.button(restartButton);
        component.winDialog.button(quitButton);
    }

    private static void createMenuDialog(final HudRenderingComponent component,
            final World world, Skin skin, LocaleService localeService, ChangeListener restartListener, ChangeListener quitListener) {

        String text = localeService._(LocalizedStrings.GameMenu.MENU_TITLE);
        component.menu = new Dialog(text, skin);

        component.menu.setSkin(skin);

        text = localeService._(LocalizedStrings.GameMenu.MENU_TEXT);
        component.menu.add(text);

        text = localeService._(LocalizedStrings.GameMenu.RESUME_BUTTON);
        TextButton resumeButton = new TextButton(text, skin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifecycleEvent lifecycleEvent = SystemEvent.createEvent(LifecycleEvent.class);
                lifecycleEvent.lifecycle = Lifecycle.PLAY;
                world.postEvent(null, lifecycleEvent);
            }
        });
        component.menu.button(resumeButton);

        text = localeService._(LocalizedStrings.GameMenu.RESTART_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(restartListener);

        text = localeService._(LocalizedStrings.GameMenu.MAIN_MENU_BUTTON);
        TextButton quitButton = new TextButton(text, skin);
        quitButton.addListener(quitListener);

        component.menu.button(restartButton);
        component.menu.button(quitButton);
    }

    private static void createUpperHud(HudRenderingComponent component, Skin skin, TaflWorld gameWorld) {

        HudBackground upperBackground =
                createBackground(Constants.GameConstants.GAME_HEIGHT - Constants.HudConstants.HUD_HEIGHT);
        component.hubStage.addActor(upperBackground);

        Table table = new Table(skin);

        createButtons(component, skin, table, gameWorld);

        component.turn = new Label("", skin);
        table.add(component.turn).expandX();

        component.gameTime = new Label("", skin);
        table.add(component.gameTime).expandX();

        if (Constants.GameConstants.DEBUG) {
            component.mouseLocation = new Label("", skin);
            table.add(component.mouseLocation).expandX();

            component.fps = new Label("", skin);
            table.add(component.fps).expandX();
        }

        table.right().top();
        table.setPosition(0, Constants.GameConstants.GAME_HEIGHT);
        table.setWidth(Constants.GameConstants.GAME_WIDTH);
        component.hubStage.addActor(table);
    }

    private static void createLowerHud(HudRenderingComponent component, Skin skin, LocaleService localeService) {
        HudBackground lowerBackground = createBackground(0);
        component.hubStage.addActor(lowerBackground);

        Table table = new Table();

        component.log = new List(new Object[] {}, skin);
        table.add(component.log).expandX();
        table.row();

        table.right().top().setPosition(0, Constants.HudConstants.HUD_HEIGHT);
        table.setWidth(Constants.GameConstants.GAME_WIDTH);
        component.hubStage.addActor(table);
    }

    private static void createButtons(HudRenderingComponent component,
            Skin skin, Table table, final TaflWorld gameWorld) {

        LocaleService localeService = gameWorld.game.getLocaleService();
        final World world = gameWorld.world;

        String text = localeService._(LocalizedStrings.Hud.MENU_BUTTON);
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifecycleEvent lifecycleEvent = SystemEvent.createEvent(LifecycleEvent.class);
                lifecycleEvent.lifecycle = Lifecycle.MENU;
                world.postEvent(null, lifecycleEvent);
            }
        });
        table.add(button).size(Constants.HudConstants.BUTTON_WIDTH, Constants.HudConstants.BUTTON_HEIGHT).uniform();
    }

    private static HudBackground createBackground(float y) {
        HudBackground background = new HudBackground();
        background.setPosition(0, y);
        background.setWidth(Constants.GameConstants.GAME_WIDTH);
        background.setHeight(Constants.HudConstants.HUD_HEIGHT);
        background.setColor(Constants.HudConstants.HUD_BACKGROUND_COLOR);
        return background;
    }
}
