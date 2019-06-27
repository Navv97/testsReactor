package edu.iis.mto.testreactor.exc4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private RunResult runResult;

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

    @Test
    public void testIfDirtFilterCapacityIsAcceptable(){
        programConfiguration = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).build();
        when(door.closed()).thenReturn(false);
        when(dirtFilter.capacity()).thenReturn(35.0d);
        assertEquals(dishWasher.start(programConfiguration).getStatus(),Status.ERROR_FILTER);
    }

    @Test
    public void pourMethodShouldThrowExceptionAndErrorPump(){
        programConfiguration = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).build();
        when(door.closed()).thenReturn(false);
        when(dirtFilter.capacity()).thenReturn(60.0d);
        try {
            Mockito.doThrow(PumpException.class).when(waterPump).pour(programConfiguration.getProgram());
        } catch (PumpException e) {
            e.printStackTrace();
        }
        assertEquals(dishWasher.start(programConfiguration).getStatus(),Status.ERROR_PUMP);
    }

    @Test
    public void runProgramMethodShouldThrowExceptionAndErrorProgram(){
        programConfiguration = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).build();
        when(door.closed()).thenReturn(false);
        when(dirtFilter.capacity()).thenReturn(60.0d);
        try {
            Mockito.doThrow(EngineException.class).when(engine).runProgram(programConfiguration.getProgram().getTimeInMinutes());
        } catch (EngineException e) {
            e.printStackTrace();
        }
        assertEquals(dishWasher.start(programConfiguration).getStatus(),Status.ERROR_PROGRAM);
    }

    @Test
    public void drainMethodShouldThrowExceptionAndErrorPump(){
        programConfiguration = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).build();
        when(door.closed()).thenReturn(false);
        when(dirtFilter.capacity()).thenReturn(60.0d);
        try {
            Mockito.doThrow(PumpException.class).when(waterPump).drain();
        } catch (PumpException e) {
            e.printStackTrace();
        }
        assertEquals(dishWasher.start(programConfiguration).getStatus(),Status.ERROR_PUMP);
    }

}
