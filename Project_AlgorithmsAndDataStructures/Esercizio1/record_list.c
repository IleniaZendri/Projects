#include "list.h"
#include <stdlib.h>
#include <stdio.h>
#include <stddef.h>

/*
	Author: Murazzano Luca, Zendri Ilenia;
 */

struct _Node{
	void* elem;
	struct _Node* next;
	struct _Node* prev;
};

struct _List{
	struct _Node* head;
	struct _Node* tail;
	size_t size;
};

struct _Iterator{
	struct _Node* curr;
};

//return a new empty list
List* new_empty_list(unsigned int size){
	List* recordlist=malloc(sizeof(List));
	recordlist->head=NULL;
	recordlist->tail=NULL;
	recordlist->size=0;
	return recordlist;
}

//retrun a new node with the specified element
Node* new_node(void* elemt){
	Node* node=(Node*)malloc(sizeof(Node));
	node->elem=elemt;
	node->next=NULL;
	node->prev=NULL;
	return node;
}

//return a new iterator
Iterator* new_iterator(List* list){
	Iterator* iterator=(Iterator*)malloc(sizeof(Iterator));
	iterator->curr=list->head;
	return iterator;
}

// Returns 1 if list is empty, 0 otherwise
int list_empty(List* list){
	if(list->size==0)
		return 1;
	else
		return 0;
}

// Insert element at the tail of the list
void insert_tail(List* list, void* elem){
	if(list_empty(list)){
		Node* node=new_node(elem);
		list->head=node;
		list->tail=node;
		list->size++;
	}else{
		Node* node=new_node(elem);
		node->prev=list->tail;
		list->tail->next=node;
		list->tail=node;
		list->size++;
	}
}

// Insert element at the specific index of the list
int insert_at(List* list, void* elem, int index){
	if(index<=list_size(list)){
		int idx=0;
		Iterator* iterator= new_iterator(list);
		while(idx<index-1 && iterator_valid(iterator, list)){
			iterator_next(iterator, list);
			idx++;
		}
		Node* node=new_node(elem);
		iterator->curr->prev->next=node;
		node->prev=iterator->curr->prev;
		iterator->curr->prev=node;
		node->next=iterator->curr;
		list->size++;
		iterator_delete(iterator);
		return 1;
	}else{
		return 0;
	}
}

// Delete element at at the tail of the list
void delete_tail(List* list){
	list->tail=list->tail->prev;
	list->tail->next=NULL;
	list->size--;
}

// Delete element at a specific index of the list
void delete_at(List* list, int index){ //list_free
	Iterator* iterator= new_iterator(list);
	if(index==0){
		list->head=iterator->curr->next;
		list->head->prev=NULL;
	}else{
		int idx=0;
		while(idx<index-1 && iterator_valid(iterator, list)){
			iterator_next(iterator, list);
			idx++;
		}
		iterator->curr->prev->next=iterator->curr->next;
		iterator->curr->next->prev=iterator->curr->prev;
	}
	list->size--;
	iterator_delete(iterator);
}

// Returns the element of the list at the specified position
void* get_elem_at(List* list, int index){
	if(index<=list_size(list)){
		Iterator* iterator= new_iterator(list);
		int idx=0;
		while(idx<index && iterator_valid(iterator, list)){
			iterator_next(iterator, list);
			idx++;
		}
		return iterator_get_elem_at(iterator, list);
		iterator_delete(iterator);
	}else{
		return NULL;
	}
}

// Returns the number of elements currently stored in the list
unsigned int list_size(List* list){
	return list->size;
}

// Returns the elemt pointed by the iterator current index
void* iterator_get_elem_at(Iterator* iterator , List* list){
	return iterator->curr->elem;
}

// Move the iterator index to the next element
void iterator_next(Iterator* iterator, List* list){
	if(iterator_valid(iterator, list)){
		iterator->curr=iterator->curr->next;
	}else{
		
	}
}

// Check if the iterator is still valid
int iterator_valid(Iterator* iterator, List* list){
	if(iterator->curr==NULL){
		return 0;
	}else{
		return 1;
	}
}

// Destroy the list
void list_delete(List* list){
	free(list->head);
	free(list);
	list=NULL;
}

//Destroy the Iterator
void iterator_delete(Iterator* iterator){
	free(iterator);
	iterator=NULL;
}

void print_list(List* list){
	Iterator* iterator= new_iterator(list);
	while(iterator_valid(iterator, NULL)){
		fprintf(stderr, "elemento: %ld\n", (long)iterator->curr->elem);
		iterator_next(iterator, list);
	}
	//iterator_next(iterator, list);
	//fprintf(stderr, "elemento: %ld\n", (long)iterator->curr->elem);
	iterator_delete(iterator);
}

/*int main(){
	List* list=New_empty_list(0);
	fprintf(stderr, "%d\n", List_empty(list));
	
	void *c=(void*)12;
	Insert_tail(list, c);
	printf("Insert element\n");
	int n=List_size(list);
	printf("Size of the list: %d\n", n);
	
	stampaLista(list);
	
	void *a=(void*)13;
	Insert_tail(list, a);
	printf("\nInsert element\n");
	n=List_size(list);
	printf("Size of the list: %d\n", n);
	
	stampaLista(list);
	
	void *b=(void*)15;
	Insert_tail(list, b);
	printf("\nInsert element\n");
	n=List_size(list);
	printf("Size of the list: %d\n", n);
	
	stampaLista(list);
	
	void *d=(void*)17;
	Insert_at(list, d, 2);
	printf("\nInsert element\n");
	n=List_size(list);
	printf("Size of the list: %d\n", n);
	
	stampaLista(list);

	Delete_elem_tail(list);
	Delete_elem_at(list, 0);
	d=Get_elem_at(list, 0);
	printf("\nGet elem at 1: %ld\n", (long)d);
	printf("Delete element\n\n");

	stampaLista(list);
}*/

