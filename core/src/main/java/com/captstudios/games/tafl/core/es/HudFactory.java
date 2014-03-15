package com.captstudios.games.tafl.core.es;

import com.artemis.World;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.captstudios.games.tafl.core.TaflGame;
import com.captstudios.games.tafl.core.consts.Assets;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.consts.LocalizedStrings;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.es.components.singleton.HudRenderingComponent;
import com.captstudios.games.tafl.core.es.systems.events.HintEvent;
import com.captstudios.games.tafl.core.es.systems.events.LifeCycleEvent;
import com.captstudios.games.tafl.core.es.systems.events.UndoEvent;
import com.captstudios.games.tafl.core.utils.device.DeviceSettings;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.esotericsoftware.tablelayout.Cell;
import com.roundtriangles.games.zaria.services.resources.LocaleService;


public class HudFactory {

    public static void populateHudRenderingComponent(
            HudRenderingComponent component,
            TaflWorld gameWorld) {

        component.hubStage = gameWorld.stage;

        Skin skin = gameWorld.game.graphicsService.getSkin(Assets.Skin.UI_SKIN);

        createUpperHud(component, skin, gameWorld);

        createDialogs(component, gameWorld, skin, gameWorld.game.localeService);
    }

    private static void createDialogs(final HudRenderingComponent component, final TaflWorld gameWorld, Skin skin, LocaleService ls) {

        ChangeListener resumeListener = new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifeCycleEvent lifecycleEvent = SystemEvent.createEvent(LifeCycleEvent.class);
                lifecycleEvent.lifecycle = LifeCycle.PLAY;
                gameWorld.world.postEvent(null, lifecycleEvent);
                gameWorld.game.soundService.play();
            }
        };

        ChangeListener restartListener = new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifeCycleEvent lifecycleEvent = SystemEvent.createEvent(LifeCycleEvent.class);
                lifecycleEvent.lifecycle = LifeCycle.RESTART;
                gameWorld.world.postEvent(null, lifecycleEvent);
                gameWorld.game.soundService.play();
            }
        };

        ChangeListener quitListener = new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifeCycleEvent lifecycleEvent = SystemEvent.createEvent(LifeCycleEvent.class);
                lifecycleEvent.lifecycle = LifeCycle.QUIT;
                gameWorld.world.postEvent(null, lifecycleEvent);
                gameWorld.game.soundService.play();
            }
        };

        createMenuDialog(component, gameWorld, skin, ls, resumeListener, restartListener, quitListener);
        createWinDialog(component, gameWorld, skin, ls, restartListener, quitListener);
        createLossDialog(component, gameWorld, skin, ls, restartListener, quitListener);
        createDrawDialog(component, gameWorld, skin, ls, restartListener, quitListener);
        createSurrenderDialog(component, gameWorld, skin, ls, restartListener, quitListener);
    }

    private static void createLossDialog(HudRenderingComponent component,
            TaflWorld gameWorld,
            Skin skin,
            LocaleService ls,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        component.lossDialog = createDialog(gameWorld.game, LocalizedStrings.GameMenu.GAME_OVER_TITLE,
                skin, ls, gameWorld.game.deviceSettings);

        addText(LocalizedStrings.GameMenu.LOSS_TEXT, skin, ls, component.lossDialog);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.RESTART_BUTTON, skin, ls, restartListener, component.lossDialog);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.MAIN_MENU_BUTTON, skin, ls, quitListener, component.lossDialog);
    }

    private static void createSurrenderDialog(HudRenderingComponent component,
            TaflWorld gameWorld,
            Skin skin,
            LocaleService ls,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        component.surrenderDialog = createDialog(gameWorld.game, LocalizedStrings.GameMenu.GAME_OVER_TITLE,
                skin, ls, gameWorld.game.deviceSettings);

        addText(LocalizedStrings.GameMenu.SURRENDER_TEXT, skin, ls, component.surrenderDialog);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.RESTART_BUTTON, skin, ls, restartListener, component.surrenderDialog);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.MAIN_MENU_BUTTON, skin, ls, quitListener, component.surrenderDialog);
    }

    private static void createDrawDialog(HudRenderingComponent component,
            TaflWorld gameWorld,
            Skin skin,
            LocaleService ls,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        component.drawDialog = createDialog(gameWorld.game, LocalizedStrings.GameMenu.GAME_OVER_TITLE,
                skin, ls, gameWorld.game.deviceSettings);

        component.drawText = addText(skin, component.drawDialog);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.RESTART_BUTTON, skin, ls, restartListener, component.drawDialog);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.MAIN_MENU_BUTTON, skin, ls, quitListener, component.drawDialog);
    }

    private static void createWinDialog(HudRenderingComponent component,
            TaflWorld gameWorld,
            Skin skin,
            LocaleService ls,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        component.winDialog = createDialog(gameWorld.game, LocalizedStrings.GameMenu.GAME_OVER_TITLE,
                skin, ls, gameWorld.game.deviceSettings);

        component.winText = addText(skin, component.winDialog);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.RESTART_BUTTON, skin, ls, restartListener, component.winDialog);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.MAIN_MENU_BUTTON, skin, ls, quitListener, component.winDialog);
    }

    private static void createMenuDialog(HudRenderingComponent component,
            final TaflWorld gameWorld,
            Skin skin,
            LocaleService ls,
            ChangeListener resumeListener,
            ChangeListener restartListener,
            ChangeListener quitListener) {

        component.menu = createDialog(gameWorld.game, LocalizedStrings.GameMenu.MENU_TITLE,
                skin, ls, gameWorld.game.deviceSettings);

        addButton(gameWorld.game, LocalizedStrings.GameMenu.RESUME_BUTTON, skin, ls, resumeListener, component.menu);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.RESTART_BUTTON, skin, ls, restartListener, component.menu);
        addButton(gameWorld.game, LocalizedStrings.GameMenu.MAIN_MENU_BUTTON, skin, ls, quitListener, component.menu);
    }

    private static Label addText(Skin skin, Dialog dialog) {
        Label label = new Label("", skin, Assets.Skin.SKIN_STYLE_DIALOG);
        dialog.text(label);
        return label;
    }


    private static void addText(Object text, Skin skin, LocaleService ls, Dialog dialog) {
        Label label = new Label(ls.get(text), skin, Assets.Skin.SKIN_STYLE_DIALOG);
        dialog.text(label);
    }

    private static void addButton(TaflGame game, Object text, Skin skin, LocaleService ls, ChangeListener listener, Dialog dialog) {

        Sprite up = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.BLANK);
        Sprite down = game.graphicsService.getSprite(Assets.GraphicFiles.ATLAS_PIECES, Assets.ButtonGraphics.BLANK_PRESSED);

        TextButtonStyle style = new TextButtonStyle();
        style.fontColor = Constants.ScreenConstants.TEXT_COLOR;
        style.font = game.graphicsService.getFont(game.deviceSettings.hudFont);
        style.up = new TextureRegionDrawable(new TextureRegion(up));
        style.down = new TextureRegionDrawable(new TextureRegion(down));

        TextButton button = new TextButton(ls.get(text), style);

        button.addListener(listener);
        dialog.button(button);
        dialog.getButtonTable().row();
    }

    private static Dialog createDialog(TaflGame game, Object title, Skin skin, LocaleService ls, DeviceSettings deviceSettings) {
        Dialog dialog = new Dialog(ls.get(title), skin, Assets.Skin.SKIN_STYLE_DIALOG);
        dialog.setMovable(false);
        dialog.setModal(true);

        dialog.padTop(50);

        dialog.getCell(dialog.getButtonTable()).pad(deviceSettings.dialogSpacing);
        dialog.getContentTable().defaults().padTop(deviceSettings.dialogSpacing);
        dialog.getContentTable().defaults().padLeft(deviceSettings.dialogSpacing);
        dialog.getContentTable().defaults().padRight(deviceSettings.dialogSpacing);
        dialog.getButtonTable().defaults().spaceTop(deviceSettings.dialogSpacing);
        dialog.getButtonTable().defaults().spaceBottom(deviceSettings.dialogSpacing);
        dialog.getButtonTable().defaults().padLeft(deviceSettings.dialogSpacing);
        dialog.getButtonTable().defaults().padRight(deviceSettings.dialogSpacing);
        dialog.getButtonTable().defaults().size(deviceSettings.dialogButtonWidth,
                deviceSettings.dialogButtonHeight);

        return dialog;
    }

    private static void createUpperHud(HudRenderingComponent component, Skin skin, TaflWorld gameWorld) {

        Table table = new Table(skin);

        createMenu(component, skin, table, gameWorld);
        createMute(component, skin, table, gameWorld);
        createHint(component, skin, table, gameWorld);
        createUndo(component, skin, table, gameWorld);

        if (Constants.GameConstants.DEBUG) {
            table.row().padTop(gameWorld.game.deviceSettings.hudPlayerLablePad);
            component.fps = new Label("", skin, Assets.Skin.SKIN_STYLE_DIALOG);
            table.add(component.fps).colspan(4);
        }

        table.row().padTop(gameWorld.game.deviceSettings.hudPlayerLablePad);

        createPlayerLabels(skin, table, gameWorld);

        table.padTop(gameWorld.game.deviceSettings.hudTablePadding);
        table.right().top().setFillParent(true);

        if (Constants.GameConstants.DEBUG) {
            table.debug();
        }
        component.hubStage.addActor(table);
    }

    private static void createPlayerLabels(Skin skin, Table table, TaflWorld gameWorld) {

        Table innerTable = new Table(skin);
        String blackText;
        String whiteText;
        if (gameWorld.match.computerStarts) {
            blackText = gameWorld.game.localeService.get(LocalizedStrings.Game.COMPUTER_PLAYER);
            whiteText = gameWorld.game.localeService.get(LocalizedStrings.Game.HUMAN_PLAYER);
        } else {
            blackText = gameWorld.game.localeService.get(LocalizedStrings.Game.HUMAN_PLAYER);
            whiteText = gameWorld.game.localeService.get(LocalizedStrings.Game.COMPUTER_PLAYER);
        }

        Image blackIcon = new Image(gameWorld.game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.BLACK));

        Label blackLabel = new Label(blackText, skin, Assets.Skin.SKIN_STYLE_PLAYER_TAG);

        innerTable.add(blackIcon).padLeft(
                gameWorld.game.deviceSettings.hudTablePadding).padRight(
                        gameWorld.game.deviceSettings.hudTablePadding);
        innerTable.add(blackLabel).left().expandX();

        Image whiteIcon = new Image(gameWorld.game.graphicsService.getSprite(
                Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.WHITE));

        Label whiteLabel = new Label(whiteText, skin, Assets.Skin.SKIN_STYLE_PLAYER_TAG);

        innerTable.add(whiteLabel).align(BaseTableLayout.RIGHT).expandX();
        innerTable.add(whiteIcon).padLeft(
                gameWorld.game.deviceSettings.hudTablePadding).padRight(
                        gameWorld.game.deviceSettings.hudTablePadding);

        Cell<?> innerTableCell = table.add(innerTable).colspan(4);
        innerTableCell.size(Gdx.graphics.getWidth(), 50);
    }

    private static void createUndo(HudRenderingComponent component,
            Skin skin, Table table, TaflWorld gameWorld) {

        final World world = gameWorld.world;

        TextureRegion textureRegion = new TextureRegion(
                gameWorld.game.graphicsService.getSprite(
                        Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.UNDO));
        Drawable imageUp = new TextureRegionDrawable(textureRegion);
        ImageButton button = new ImageButton(imageUp);

        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                UndoEvent undoEvent = SystemEvent.createEvent(UndoEvent.class);
                world.postEvent(null, undoEvent);
            }
        });
        table.add(button).size(gameWorld.game.deviceSettings.hudButtonWidth,
                gameWorld.game.deviceSettings.hudButtonHeight).expandX();
    }

    private static void createHint(HudRenderingComponent component,
            Skin skin, Table table, TaflWorld gameWorld) {

        final World world = gameWorld.world;

        TextureRegion textureRegion = new TextureRegion(
                gameWorld.game.graphicsService.getSprite(
                        Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.HINT));
        Drawable imageUp = new TextureRegionDrawable(textureRegion);
        ImageButton button = new ImageButton(imageUp);

        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                HintEvent undoEvent = SystemEvent.createEvent(HintEvent.class);
                world.postEvent(null, undoEvent);
            }
        });
        table.add(button).size(gameWorld.game.deviceSettings.hudButtonWidth,
                gameWorld.game.deviceSettings.hudButtonHeight).expandX();
    }

    private static void createMute(HudRenderingComponent component,
            Skin skin, Table table, final TaflWorld gameWorld) {

        TextureRegion textureRegion = new TextureRegion(
                gameWorld.game.graphicsService.getSprite(
                        Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.MUTE));

        final Drawable muteDrawable = new TextureRegionDrawable(textureRegion);

        textureRegion = new TextureRegion(
                gameWorld.game.graphicsService.getSprite(
                        Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.UN_MUTE));

        final Drawable unmuteDrawable = new TextureRegionDrawable(textureRegion);

        final ImageButton button;
        if (gameWorld.game.preferenceService.isSoundEnabled()) {
            button = new ImageButton(muteDrawable);
        } else {
            button = new ImageButton(unmuteDrawable);
        }

        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if (gameWorld.game.preferenceService.isSoundEnabled()) {
                    gameWorld.game.preferenceService.setSoundEnabled(false);
                    button.getStyle().imageUp = unmuteDrawable;
                } else {
                    gameWorld.game.preferenceService.setSoundEnabled(true);
                    button.getStyle().imageUp = muteDrawable;
                }
            }
        });
        table.add(button).size(gameWorld.game.deviceSettings.hudButtonWidth,
                gameWorld.game.deviceSettings.hudButtonHeight).expandX();
    }

    private static void createMenu(HudRenderingComponent component,
            Skin skin, Table table, final TaflWorld gameWorld) {

        final World world = gameWorld.world;

        TextureRegion textureRegion = new TextureRegion(
                gameWorld.game.graphicsService.getSprite(
                        Assets.GraphicFiles.ATLAS_PIECES, Assets.Icon.MENU));
        Drawable imageUp = new TextureRegionDrawable(textureRegion);
        ImageButton button = new ImageButton(imageUp);

        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                LifeCycleEvent lifecycleEvent = SystemEvent.createEvent(LifeCycleEvent.class);
                lifecycleEvent.lifecycle = LifeCycle.MENU;
                world.postEvent(null, lifecycleEvent);
            }
        });
        table.add(button).size(gameWorld.game.deviceSettings.hudButtonWidth,
                gameWorld.game.deviceSettings.hudButtonHeight).expandX();
    }
}
