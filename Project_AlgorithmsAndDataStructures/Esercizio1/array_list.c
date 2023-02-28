#include "list.h"
#include <stdlib.h>
#include <stdio.h>
#include <stddef.h>

/*
	Author: Murazzano Luca, Zendri Ilenia;
 */

struct _List{
	void** array_list;
	size_t capacity;
	unsigned int size;
};

struct _Iterator{
	void* array_list;
	int index;
};

//return a new empty list
List* new_empty_list(unsigned int capacity){
	List* arraylist=(List*)malloc(sizeof(List));
	arraylist->capacity=capacity;
	arraylist->size=0;
	arraylist->array_list=(void**)malloc(sizeof(void*)*capacity);
	return arraylist;
}

//return a new iterator
Iterator* new_iterator(List* input_list){
	Iterator* iterator=(Iterator*)malloc(sizeof(Iterator));
	iterator->array_list=input_list;
	iterator->index=0;
	return iterator;
}

// Returns 1 if list is empty, 0 otherwise
int list_empty(List* list){
	if(list->size==0)
		return 1;
	else
		return 0;
}

//realloc the arraylist, with a double capacity array
List* array_extend(List* list){
	list=realloc(list, sizeof(void*)*(list->capacity)*2);
	list->capacity=(list->capacity)*2;
	return list;
}

// Insert element at the tail of the list
void insert_tail(List* list, void* elem){
	if(list->capacity == list->size){
		List* new_list=array_extend(list);
		list=new_list;
	}
	list->array_list[list->size]=elem;
	list->size++;
}

// Insert element at the specific index of the list
int insert_at(List* list, void* elem, int index){
	int tmp=list->size;
	Iterator* iterator= new_iterator(list);
	while(iterator->index!=index && iterator_valid(iterator, list)){
		iterator_next(iterator, list);
	}
	if(list->size==list->capacity){
		list=array_extend(list);
	}
	while(tmp>index){
		list[tmp]=list[tmp-1];
		tmp--;
	}
	list->array_list[iterator->index]=elem;
	list->size++;
	iterator_delete(iterator);
	return 1;
}

// Delete element at at the tail of the list
void delete_tail(List* list){
	list->size--;
}

// Delete element at a specific index of the list
void delete_at(List* list, int index){
	Iterator* iterator= new_iterator(list);
	while(iterator->index!=index && iterator_valid(iterator, list)){
		iterator_next(iterator, list);
	}
	iterator_next(iterator, list);
	while(iterator->index!=list->size && iterator_valid(iterator, list)){
		list->array_list[iterator->index-1]=list->array_list[iterator->index];
		iterator_next(iterator, list);
	}
	list->size--;
	iterator_delete(iterator);
}

// Returns the element of the list at the specified position
void* get_elem_at(List* list, int index){
	Iterator* iterator= new_iterator(list);
	while(iterator->index!=index && iterator_valid(iterator, list)){
		iterator_next(iterator, list);
	}
	return iterator_get_elem_at(iterator, list);
	
	iterator_delete(iterator);
}

// Returns the number of elements currently stored in the list
unsigned int list_size(List* list){
	return list->size;
}

// Returns the elemt pointed by the iterator current index
void* iterator_get_elem_at(Iterator* iterator, List* list){
	return list->array_list[iterator->index];
}

// Move the iterator index to the next element
void iterator_next(Iterator* iterator, List* list){
		iterator->index++;
}

// Check if the iterator is still valid, returns 1 if iterator is valid, 0 otherwise
int iterator_valid(Iterator* iterator, List* list){
	if(iterator->index > list->capacity-1){
		return 0;
	}else{
		return 1;
	}
}

// Destroy the list
void list_delete(List* list){
	free(list->array_list);
	free(list);
	list=NULL;
}

//Destroy the Iterator
void iterator_delete(Iterator* iterator){
	free(iterator);
	iterator=NULL;
}

void print_list(List* list){
	fprintf(stderr, "\n");
	fprintf(stderr, "\nCapacita' lista: %ld\n", (long)list->capacity);
	for (int i = 0; i < list->size; i++){
		fprintf(stderr, "\nElemento %d : %ld\n", i + 1, (long)list->array_list[i]);
	}
	return;
}

List* list;

/*
int main(){
	list= New_empty_list(2);

	stampaLista(list);	
	printf("*******************************\n\n");
	
	void *a=(void*)8;
	Insert_tail(list, a);
	printf("Insert element\n");
	int n=List_size(list);
	printf("Size of the list: %d\n", n);

	stampaLista(list);
	printf("*******************************\n\n");

	void *b=(void*)10;
	Insert_tail(list, b);
	printf("Insert element\n");
	n=List_size(list);
	printf("Size of the list: %d\n", n);

	stampaLista(list);
	printf("*******************************\n\n");	
	
	void *c=(void*)12;
	Insert_tail(list, c);
	printf("Insert element\n");
	n=List_size(list);
	printf("Size of the list: %d\n", n);
	
	stampaLista(list);
	printf("*******************************\n\n");

	Delete_elem_tail(list);
	printf("Delete element\n");
	n=List_size(list);
	printf("Size of the list: %d\n", n);
	
	stampaLista(list);
	printf("*******************************\n\n");

	Delete_elem_at(list, 1);
	printf("Delete element\n");
	n = List_size(list);
	printf("Size of the list: %d\n", n);
	
	stampaLista(list);
	printf("*******************************\n\n");
	
	void *d=(void*)14;
	Insert_tail(list, d);
	printf("Insert element\n");
	n=List_size(list);
	printf("Size of the list: %d\n", n);

	stampaLista(list);
	printf("*******************************\n\n");

}
*/
