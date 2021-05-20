import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;
import com.pokemons.*;

public class Main{
	
	public static void main(String[] args) {
		Battle b = new Battle();
		Pokemon p1 = new Pokemon("Poke", 1);
		Pokemon p2 = new Pokemon("Spoke", 1);
		Lugia p3 = new Lugia("Lugigi", 1);
		Gloom p4 = new Gloom("Gloomiee", 1);
		Magcargo p5 = new Magcargo("Magcargo", 1);
		Oddish p6 = new Oddish("Oddy", 1);
		Slugma p7 = new Slugma("Sluggy", 1);
		Vileplume p8 = new Vileplume("Villy", 1);
		b.addAlly(p1);
		b.addFoe(p2);
		b.addAlly(p3);
		b.addFoe(p4);
		b.addAlly(p5);
		b.addFoe(p6);
		b.addAlly(p7);
		b.addFoe(p8);
		b.go();
	
	}

}
