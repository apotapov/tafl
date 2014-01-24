package com.pactstudios.games.tafl.core.es;

import com.artemis.World;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.Gdx;
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
import com.pactstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.pactstudios.games.tafl.core.es.systems.render.hud.HudBackground;
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

    private static void createWinDialog(HudRenderingComponent component,
            World world, Skin skin, LocaleService localeService, ChangeListener restartListener,
            ChangeListener quitListener) {

        String text = localeService.get(LocalizedStrings.GameMenu.WIN_TITLE);
        component.winDialog = new Dialog(text, skin);

        component.winDialog.setSkin(skin);

        text = localeService.get(LocalizedStrings.GameMenu.WIN_TEXT);
        component.winDialog.add(text);

        text = localeService.get(LocalizedStrings.GameMenu.RESTART_BUTTON);
        TextButton restartButton = new TextButton(text, skin);
        restartButton.addListener(restartListener);

        text = localeService.get(LocalizedStrings.GameMenu.MAIN_MENU_BUTTON);
        TextButton quitButton = new TextButton(text, skin);
        quitButton.addListener(quitListener);

        component.winDialog.button(restartButton);
        component.winDialog.button(quitButton);
    }

    private static void createMenuDialog(final HudRenderingComponent component,
            final World world, Skin skin, LocaleService localeService, ChangeListener restartListener, ChangeListener quitListener) {

        String text = localeService.get(LocalizedStrings.GameMenu.MENU_TITLE);
        component.menu = new Dialog(text, skin);

        component.menu.setSkin(skin);

        text = localeService.get(LocalizedStrings.GameMenu.MENU_TEXT);
        component.menu.add(text);

        text = localeService.get(LocalizedStrings.GameMenu.RESUME_BUTTON);
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

        HudBackground upperBackground =
                createBackground(Gdx.graphics.getHeight() - Constants.HudConstants.HUD_HEIGHT);
        component.hubStage.addActor(upperBackground);

        Table table = new Table(skin);

        createButtons(component, skin, table, gameWorld);

        component.turn = new Label("", skin);
        table.add(component.turn).expandX();

        component.timer = new Label("", skin);
        table.add(component.timer).expandX();

        if (Constants.GameConstants.DEBUG) {
            component.mouseLocation = new Label("", skin);
            table.add(component.mouseLocation).expandX();

            component.fps = new Label("", skin);
            table.add(component.fps).expandX();
        }

        table.right().top().setFillParent(true);
        component.hubStage.addActor(table);
    }

    private static void createLowerHud(HudRenderingComponent component, Skin skin, LocaleService localeService) {
        HudBackground lowerBackground = createBackground(0);
        component.hubStage.addActor(lowerBackground);

        Table table = new Table();

        //        component.log = new List(new Object[] {}, skin);
        //        table.add(component.log).expandX();
        //        table.row();

        table.right().top().setFillParent(true);
        component.hubStage.addActor(table);
    }

    private static void createButtons(HudRenderingComponent component,
            Skin skin, Table table, final TaflWorld gameWorld) {

        final World world = gameWorld.world;

        String text = gameWorld.game.localeService.get(LocalizedStrings.Hud.MENU_BUTTON);
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
        background.setWidth(Gdx.graphics.getWidth());
        background.setColor(Constants.HudConstants.HUD_BACKGROUND_COLOR);
        return background;
    }
}
