package com.jcloisterzone.game.phase;

import java.util.List;

import com.google.common.collect.Lists;
import com.jcloisterzone.action.MeepleAction;
import com.jcloisterzone.action.PlayerAction;
import com.jcloisterzone.board.Location;
import com.jcloisterzone.board.Position;
import com.jcloisterzone.collection.Sites;
import com.jcloisterzone.figure.Meeple;
import com.jcloisterzone.figure.Phantom;
import com.jcloisterzone.game.Game;
import com.jcloisterzone.game.capability.PhantomCapability;
import com.jcloisterzone.game.capability.TowerCapability;

public class PhantomPhase extends Phase {

    private final TowerCapability towerCap;

    public PhantomPhase(Game game) {
        super(game);
        towerCap = game.getCapability(TowerCapability.class);
    }

    @Override
    public boolean isActive() {
        return game.hasCapability(PhantomCapability.class);
    }

    @Override
    public void notifyRansomPaid() {
        enter(); //recompute available actions
    }

    @Override
    public void enter() {
        List<PlayerAction> actions = Lists.newArrayList();

        Sites commonSites = game.prepareCommonSites();
        if (getActivePlayer().hasFollower(Phantom.class)) {
            if (towerCap != null) {
                towerCap.prepareCommonOnTower(commonSites);
            }
            if (!commonSites.isEmpty()) {
                actions.add(new MeepleAction(Phantom.class, commonSites));
            }
        }
        if (isAutoTurnEnd(actions)) {
            next();
        } else {
            notifyUI(actions, true);
        }
    }

    private boolean isAutoTurnEnd(List<PlayerAction> actions) {
        if (! actions.isEmpty()) return false;
        if (towerCap != null && !towerCap.isRansomPaidThisTurn() && towerCap.hasImprisonedFollower(getActivePlayer(), Phantom.class)) {
            //player can return phantom figure immediately
            return false;
        }
        return true;
    }

    @Override
    public void deployMeeple(Position p, Location loc, Class<? extends Meeple> meepleType) {
        if (!meepleType.equals(Phantom.class)) {
            throw new IllegalArgumentException("Only phantom can be placed as second follower.");
        }
        Meeple m = getActivePlayer().getUndeployedMeeple(meepleType);
        m.deploy(getBoard().get(p), loc);
        next();
    }

    @Override
    public void pass() {
        next();
    }

}
