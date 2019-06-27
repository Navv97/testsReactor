package edu.iis.mto.testreactor.exc4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DishWasherTest {

    @Mock
    private DirtFilter dirtFilter;
    @Mock
    private Door door;
    @Mock
    private Engine engine;
    @Mock
    private WaterPump waterPump;

    private DishWasher dishWasher;
    private ProgramConfiguration programConfiguration;

    @Before
    public void init(){
        dishWasher = new DishWasher(waterPump,engine,dirtFilter,door);
    }

    @Test
    public void testIfDishWasherDoorsAreOpen(){
        programConfiguration = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).build();
        when(door.closed()).thenReturn(true);
        assertEquals(dishWasher.start(programConfiguration).getStatus(),Status.DOOR_OPEN_ERROR);
    }

    @Test
    public void testIfFilterIsClean(){
        programConfiguration = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).build();
        when(door.closed()).thenReturn(false);
        assertEquals(dishWasher.start(programConfiguration).getStatus(),Status.ERROR_FILTER);
    }



}
