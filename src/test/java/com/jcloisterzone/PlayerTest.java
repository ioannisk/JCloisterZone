package com.jcloisterzone;

import com.jcloisterzone.figure.Builder;
import com.jcloisterzone.game.Game;
import com.jcloisterzone.game.PlayerSlot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;
@RunWith(JUnit4.class)

public class PlayerTest {

    private Game game;
    private Player alice;
    private Player bob;


    @Before
    public void setUp() {
        game = new Game();
        alice = new Player("Alice", 0, new PlayerSlot(0));
        bob = new Player("Bob", 1, new PlayerSlot(1));
    }

    @Test
    public void testPoints() {
        alice.addPoints(10, PointCategory.ROAD);
        alice.addPoints(20, PointCategory.BAZAAR_AUCTION);

        assertEquals(30, alice.getPoints());
        assertEquals(10, alice.getPointsInCategory(PointCategory.ROAD));
        assertEquals(20, alice.getPointsInCategory(PointCategory.BAZAAR_AUCTION));
        assertEquals(0, alice.getPointsInCategory(PointCategory.CASTLE));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void testMeeple() {
        Builder builder = new Builder(game, bob);
        exception.expect(NoSuchElementException.class);
        bob.getMeepleFromSupply(Builder.class);

        bob.addMeeple(builder);
        assertEquals(builder, bob.getMeepleFromSupply(Builder.class));
    }

}
