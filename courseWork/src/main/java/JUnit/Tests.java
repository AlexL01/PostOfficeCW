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

    @Test(expected = RuntimeException.class) // ��������� �� ��������� ����������
    public void testException() {
    	throw new RuntimeException("������");
    }
    
    @BeforeClass // ��������� ������ ������������
    public static void allTestsStarted() {
    	System.out.println("������ ������������");
    } 
    
    @AfterClass // ��������� ����� ������������
    public static void allTestsFinished() {
    	System.out.println("����� ������������");
    }
    
    @Before // ��������� ������ �����
    public void testStarted() {
    System.out.println("������ �����");
    }
    
    @After // ��������� ���������� �����
    public void testFinished() {
    	System.out.println("���������� �����");
    }
    
}
