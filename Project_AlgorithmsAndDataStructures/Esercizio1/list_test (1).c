#include "list.h"
#include "test_list.h"
#include <stdlib.h>
#include <stdio.h>
#include <stddef.h>

/*
	Author: Murazzano Luca, Zendri Ilenia;
 */

#define  assert(cond){\
            if ((cond) != 1) {\
                test_unit_print_error(__func__, __LINE__);\
            }else{\
                test_unit_print_ok(__func__);\
            }\
        }

static int numok, numko;

void test_unit_print_ok(const char *file);
void test_unit_print_error(const char *file, int line);

typedef struct _Iterator Iterator;
typedef struct _List List;

//test empty list (if(list->size==0) return 1;)
void test_list_empty(){
    List* list = new_empty_list(2);
    int a;
    assert(list_empty(list) == 1);
}

//test Insert_tail() and get_elem_at()
void test_insert_tail(){
    List* list = new_empty_list(2);
    void *a=(void*)8;
	insert_tail(list, a);
    assert((list_empty(list) == 0) && get_elem_at(list, 0) == a);
}

//test Insert_at
void test_insert_at(){
    List* list = new_empty_list(5);
    void *a=(void*)4;
    assert((insert_at(list, a, 2) == 1) == (get_elem_at(list, 2) == a));
}

// Delete element at at the tail of the list
void test_delete_tail(){
    List* list = new_empty_list(5);
    void *a=(void*)8;
	insert_tail(list, a);
    insert_tail(list, a);
    delete_tail(list);
    assert((list_empty(list) == 0) && list_size(list) == 1);
}

// Delete element at a specific index of the list
void test_delete_at(){
    List* list = new_empty_list(5);
    void *a=(void*)8;
	insert_tail(list, a);
    insert_tail(list, a);
    delete_at(list,0);
    assert((list_empty(list) == 0) && list_size(list) == 1);
}

// Returns the element of the list at the specified position
void test_get_elem_at(){
    List* list = new_empty_list(2);
    void *a=(void*)8;
	insert_tail(list, a);
    assert((list_empty(list) == 0) && get_elem_at(list, 0) == a);
}

// Returns the number of elements currently stored in the list
void test_list_size(){
    List* list = new_empty_list(2);
    void *a=(void*)8;
	insert_tail(list, a);
    int n=list_size(list);
    a=(void*)8;
	insert_tail(list, a);
    int m=list_size(list);
    assert((list_empty(list) == 0) && (m>n));
}

//return the element pointe dby the iterator
void test_iterator_get_elem_at(){
    List* list = new_empty_list(2);
    void *a=(void*)8;
	insert_tail(list, a);
    Iterator* iterator= new_iterator(list);
    assert((list_empty(list) == 0) && (iterator_get_elem_at(iterator, list) == a));
}

// Check if the iterator is still valid and test the iterator_next method
void test_iterator_valid(){
    List* list = new_empty_list(1);
    Iterator* iterator= new_iterator(list);
    iterator_next(iterator, list);
    assert(iterator_valid(iterator, list)==0);

}

//print the line of the test failure
void test_unit_print_error(const char *file, int line){
    printf("ERROR at line %d: %s\n", line, file);
    numko++;
}

//print the exit of the test success
void test_unit_print_ok(const char *file){
    printf("OK\t%s\n", file);
    numok++;
}

//print the exit status of the tests
void print_test_exit(){
    printf("\nTest ok: %d, test ko: %d\n", numok, numko);
}
