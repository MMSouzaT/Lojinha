package testes;

import numeros.Numeros;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumerosTest {
    private Numeros numeros;
    @Before
    public void setUp(){
        numeros = new Numeros();
    }
    @Test
    public void testValdidarSeEUmaUnidade() {
        boolean eUnidade = numeros.eUmaUnidade(9);
        //vou validar que a resposta é verdadeira
        Assert.assertTrue(eUnidade);
    }

    @Test
    public void testValidarSeEUmaDezena() {
        boolean eUmaUnidade = numeros.eUmaUnidade(10);
        Assert.assertFalse(eUmaUnidade);

    }

}
