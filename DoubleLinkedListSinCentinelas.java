import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedListSinCentinelas<E> implements PositionList<E>{
	/**
	 * 
	 * NodoD nos sirve para trabajar la lista como una lista de nodos
	 * que implementan position
	 * @author Lucas
	 *
	 */
	private class NodoD<E> implements Position<E> {
		private E element;
		private NodoD<E> prev;
		private NodoD<E> next;
		public NodoD (NodoD<E> a, NodoD<E> s, E e) {
			element = e;
			prev = a;
			next = s;
		}
		public E element() {
			return element;
		}
		public NodoD<E> getPrev() {
			return prev;
		}
		public NodoD<E> getNext() {
			return next;
		}
		public void setElement(E e) {
			element = e;
		}
		public void setNext(NodoD<E> n) {
			next = n;
		}
		public void setPrev(NodoD<E> n) {
			prev = n;
		}

		public String toString() {
			return element.toString();
		}
	}
	/**
	 * Retorna el iterator de la lista
	 * @author Lucas
	 *
	 * @param <E>
	 */
	private class ElementIterator<E> implements Iterator<E> {
		protected PositionList<E> lista;
		protected Position<E> cursor;
		public ElementIterator (PositionList<E> L) {
			try {
				cursor = L.first();
			} catch (EmptyListException e) {
				cursor = null;
			} 
		}
		public boolean hasNext() { 
			return cursor != null;
		}
		public E next() throws NoSuchElementException {
			if (!hasNext()) {throw new NoSuchElementException ("Error: No hay siguiente");}
			E toReturn = cursor.element();
			try {
				cursor = lista.next(cursor);
			} catch (EmptyListException | InvalidPositionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return toReturn;
			
		}
	}
	
	
	protected NodoD<E> head;
	protected NodoD<E> tail;
	protected int size;
	/**
	 * Crea una nueva lista la cual inicializa con un nodo nulo para cabeza y cola, y tamaño cero.
	 * @author Lucas
	 */
	public DoubleLinkedListSinCentinelas() {
		head = tail = new NodoD<E> (null, null, null);
		size = 0;
	}
	
	/**
	 * Consulta la cantidad de elementos de la lista.
	 * @return int Cantidad de elementos de la lista.
	 * @author Lucas
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Consulta si la lista esta vacia
	 * @return Verdadero si la lista esta vacia, falso en caso contrario
	 * @author Lucas
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Devuelve la position del primer elemento de la lista, si la lista esta vacia arroja excepcion
	 * @return Position<E>, primer elemento de la lista.
	 * @throws @link{EmptyListException} si la lista esta vacia.
	 * @author Lucas
	 */
	public Position<E> first() throws EmptyListException {
		return checkPosition(head);
	}
	/**
	 * Devuelve la position del ultimo elemento de la lista. 
	 * @return Position<E>, ultimo elemento de la lista.
	 * @throws @link{EmptyListException} si la lista esta vacia.
	 * @author Lucas
	 * 
	 */
	public Position<E> last() throws EmptyListException {
		return checkPosition(tail);
	}
	
	
	/**
	 * Este metodo privado nos sirve para validar la posicion que se pasa como parametro
	 * controla que esta no sea nula, pertenezca a la lista y la lista no este vacia
	 * @param p Position
	 * @return NodoD<E> la position a nodo
	 * @throws @link{InvalidPositionException} si no cumple alguna de las dichas
	 * @author Lucas
	 */
	private NodoD<E> checkPosition(Position<E> p) throws InvalidPositionException {
		try {
			if(size == 0)
				throw new InvalidPositionException("La lista esta vacia.");
			if(p == null)
				throw new InvalidPositionException("La posicion es nula.");
			if(p.element() == null)
				throw new InvalidPositionException("La posicion tiene un elemento nulo, i.e. fue invalidada anteriormente.");
			return (NodoD<E>) p;
		} catch(ClassCastException e) {
			throw new InvalidPositionException("P no es un nodo de lista.");
		}
}
	
	/**
	 * Devuelve la posicion del elemento siguiente a la posicion pasada por parametro.
	 * @param p Position posicion a obtener su elemento siguiente.
	 * @return Posicion del elemento siguiente a la posicion pasada por parametro.
	 * @throws @link{InvalidPositionException} si el posicion pasada por parametro es invalida o la lista esta vacia.
	 * @throws @link{BoundaryViolationException} si la posicion pasada por parametro corresponde al ultimo elemento de la lista.
	 * @author Lucas
	 */
	public Position<E> next(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		NodoD<E> d = checkPosition(p);//si el nodo es valido
		if (d == tail) {throw new BoundaryViolationException("Se cae de la lista");}
		return d.getNext();//retorna el siguiente
	}
	
	/**
	 * Devuelve la posicion del elemento anterior a la posicion pasada por parametro.
	 * @param p Position a obtener su elemento anterior.
	 * @return Posicion del elemento anterior a la posicion pasada por parametro.
	 * @throws @link{InvalidPositionException} si la posicion pasada por parametro es invalida o la lista esta vacia.
	 * @throws @link{BoundaryViolationException} si la posicion pasada por parametro corresponde al primer elemento de la lista.
	 * @author Lucas
	 */
	public Position<E> prev(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		NodoD<E> d = checkPosition(p);
		if (d == head) {throw new BoundaryViolationException("Se cae de la lista");}
		return d.getPrev();
	}
	
	/**
	 * Inserta un elemento al principio de la lista.
	 * @param element Elemento a insertar al principio de la lista.
	 * @author Lucas
	 */
	public void addFirst(E element) {
		switch (size){
			case 0 -> { // si el tamaño era 0, el nodo añadido se convierte en cabeza y cola a la vez
				head = tail = new NodoD<E> (null, null, element);
			}
			case 1 -> { // si el tamaño era 1, el nodo añadido hace que tengamos que diferenciar de cabeza y cola
				head = new  NodoD<E> (null, tail, element);
				tail.setPrev(head);
			}
			default -> { // si el tamaño es mayor a 1, podemos agregar tranqui, solo preocupandonos por la cabeza
				NodoD<E> aux = head;
				head = new NodoD<E> (null, aux, element);
				aux.setPrev(head);
			}
		}
		size++;
	}
	
	/**
	 * Inserta un elemento al final de la lista.
	 * @param element Elemento a insertar al final de la lista.
	 * @author Lucas
	 */
	public void addLast(E element) {
		switch (size){
			case 0 -> { // si el tamaño era 0, el nodo añadido se convierte en cabeza y cola a la vez
				head = tail = new NodoD<E> (null, null, element);
			}
			case 1 -> { // si el tamaño era 1, el nodo añadido hace que tengamos que diferenciar de cabeza y cola
				tail = new  NodoD<E> (head, null, element);
				head.setNext(tail);
			}
			default -> {
				NodoD<E> aux = tail;// si el tamaño es mayor a 1, podemos agregar tranqui, solo preocupandonos por la cola
				tail = new NodoD<E> (aux, null, element);
				aux.setNext(tail);
			}
		}
		size++;
	}
	
	/**
	 * Inserta un elemento luego de la positionn pasada por parametro.
	 * @param p Position despues de la cual se insertara el elemento pasado por parametro.
	 * @param element Elemento a insertar luego de la posicion pasada como parametro.
	 * @throws @link{InvalidPositionException} si la posicion es invalida o la lista esta vacia.
	 * @author Lucas
	 */
	public void addAfter(Position<E> p, E element) throws InvalidPositionException {
		NodoD<E> d = checkPosition(p);//valida que sea una posicion valida
		NodoD<E> nuevo = new NodoD<E> (d, d.getNext(), element);// si no enlazamos entre medio de p y el siguiente de p
		if (d.getNext()!=null) 
			d.getNext().setPrev(nuevo);
		d.setNext(nuevo);
		size++;
	}
	
	/**
	 * Inserta un elemento antes de la position pasada por parametro.
	 * @param p Position antes de la cual se insertara el elemento pasado por parametro.
	 * @param element Elemento a insertar luego de la posicion pasada como parametro.
	 * @throws @link{InvalidPositionException} si la posicion es invalida o la lista esta vacia.
	 * @author Lucas
	 */
	public void addBefore(Position<E> p, E element) throws InvalidPositionException{
		NodoD<E> d = checkPosition(p);//valida que sea una posicion valida
		NodoD<E> nuevo = new NodoD<E> (d.getPrev(), d, element);// si no enlazamos entre medio de p y el anterior de p
		if (d.getPrev()!= null) 
			d.getPrev().setNext(nuevo);
		d.setPrev(nuevo);
		size++;
	}
	
	/**
	 * Remueve el elemento que se encuentra en la position pasada por parametro.
	 * @param p Position del elemento a eliminar.
	 * @return element Elemento removido.
	 * @throws @link{InvalidPositionException} si la posicition es invalida o la lista esta vacia.
	 * @author Lucas
	 */	
	public E remove(Position<E> p) throws InvalidPositionException {
		NodoD<E> d = checkPosition(p);//controla posicion valida
		NodoD<E> anterior = d.getPrev();
		NodoD<E> posterior = d.getNext();
		if (anterior != null) {anterior.setNext(posterior);}//enlaza el anterior con el posterior a p
		if (posterior != null) {posterior.setPrev(anterior);}//enlaza el posterior con el anterior a p
		switch (size){
			case 1 -> head = tail = new NodoD<E>(null,null,null);  //si elimino y habia un nodo, setea en null
			case 2 -> head = tail; //Si habia 2 nodos cabeza y cola ahora son el mismo nodo
			default -> { //si habia mas de 2, controla que si eliminamos cabeza o cola, se vuelvana igualar al ultimo/primer nodo
				if (d == head) {head = posterior;}
				else if (d== tail) {tail = anterior;}
			}
		}
		E aux = (E) d.element;
		d.setElement(null);// destruye el nodo d
		d.setNext(null);
		d.setPrev(null);
		size--;
		return aux;
	}

	/**
	
	 * Establece el elemento en la position pasados por parametro. Reemplaza el elemento que se encontraba anteriormente en esa position y devuelve el elemento anterior.
	 * @param p Position a establecer el elemento pasado por parametro.
	 * @param element Elemento a establecer en la posici�n pasada por parametro.
	 * @return Elemento anterior.
	 * @throws @link{InvalidPositionException} si la posicion es invalida o la lista esta vacia.	 
	 * @author Lucas
	 */
	public E set(Position<E> p, E element) throws InvalidPositionException {
		NodoD<E> d = checkPosition(p);//valida postion
		E aux = d.element;//guarda el elemento
		d.setElement(element);//reemplaza
		return aux;
	}
	
	/**
	 * Devuelve un iterador de todos los elementos de la lista.
	 * @return Un iterador de todos los elementos de la lista.
	 * @author Lucas
	 */
	public Iterator<E> iterator() {
		return new ElementIterator<E>(this);
	}
	
	/**
	 * Devuelve una coleccion iterable de posiciones.
	 * @return Una coleccion iterable de posiciones.
	 * @author Lucas
	 */
	public Iterable<Position<E>> positions(){
		PositionList<Position<E>> p = new DoubleLinkedListSinCentinelas<Position<E>>();
		if(!isEmpty()) {
			Position<E> pos = head;
			while (pos!= tail) {
				p.addLast(pos);
				try {
					pos = next(pos);
				} catch (InvalidPositionException | BoundaryViolationException e) {
					//Esto no pasa porque controlamos el ultimo
					e.printStackTrace();
				}
			}
			p.addLast(pos);
		}
		return p;
	}
}
