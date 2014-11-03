import com.epam.training.Equalator
import com.epam.training.Person

List<Person> persons = [new Person(name: 'a', age: 20),new Person(name: 'b', age: 30),new Person(name: 'c', age: 40)]
Equalator eq = new Equalator()
int num = eq.num(persons, new Person(name: 'foo', age: 20), { p1, p2 ->
    p1.age == p2.age;
});
println "persons : $num";