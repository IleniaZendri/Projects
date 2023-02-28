/*
	Author: Murazzano Luca, Zendri Ilenia;
 */

typedef struct _Iterator Iterator;
typedef struct _List List;
typedef struct _Node Node;

List* new_empty_list(unsigned int capacity);

Iterator* new_iterator(List* list);

// Returns 1 if list is empty, 0 otherwise
int list_empty(List* list);

// Insert element at the tail of the list
void insert_tail(List* list, void* element);

// Insert element at the specific index of the list
int insert_at(List* list, void* element, int index);

// Delete element at at the tail of the list
void delete_tail(List* list);

// Delete element at a specific index of the list
void delete_at(List* list, int index);

// Returns the element of the list at the specified position
void* get_elem_at(List* list, int index);

// Returns the number of elements currently stored in the list
unsigned int list_size(List* list);

// Returns the elemt pointed by the iterator current index
void* iterator_get_elem_at(Iterator* iterator, List* list);

// Move the iterator index to the next element
void iterator_next(Iterator* iterator, List* list);

// Check if the iterator is still valid
int iterator_valid(Iterator* iterator, List* list);

// Destroy the list
void list_delete(List* list);

//Destroy the Iterator
void iterator_delete(Iterator* iterator);

//print the content of the list
void print_list(List* list);






