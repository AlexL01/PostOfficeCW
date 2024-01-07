package JUnit;

import org.junit.*;

import classes.Magazine;

public class Tests {
	Magazine magazine = new Magazine();
	Magazine magazine1 = new Magazine();
	boolean b = magazine1.SetAmount(1);

    @Test
    public void testIsMagazine() {
        Assert.assertTrue(magazine.SetAmount(1)); 
    }
    
    @Test
    public void testNonIsMagazine() {
        Assert.assertFalse(magazine.SetAmount(-1));
    }
    
    @Test
    public void testMagazine() {
    	Assert.assertEquals(1, magazine1.GetAmount()); 
    	Assert.assertNotEquals(5, magazine1.GetAmount());
    }

    @Test(expected = RuntimeException.class) // Проверяем на появление исключения
    public void testException() {
    	throw new RuntimeException("Ошибка");
    }
    
    @BeforeClass // Фиксируем начало тестирования
    public static void allTestsStarted() {
    	System.out.println("Начало тестирования");
    } 
    
    @AfterClass // Фиксируем конец тестирования
    public static void allTestsFinished() {
    	System.out.println("Конец тестирования");
    }
    
    @Before // Фиксируем запуск теста
    public void testStarted() {
    System.out.println("Запуск теста");
    }
    
    @After // Фиксируем завершение теста
    public void testFinished() {
    	System.out.println("Завершение теста");
    }
    
}
