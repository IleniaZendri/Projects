/*
	Author: Murazzano Luca, Zendri Ilenia;
 */

typedef struct _Iterator Iterator;
typedef struct _List List;
typedef struct _Node Node;

List* new_empty_list(unsigned int capacity);

Iterator* new_iterator(List* list);

// Returns 1 if list is empty, 0 otherwise
void test_list_empty();

// Insert element at the tail of the list
void test_insert_tail();

// Insert element at the specific index of the list
void test_insert_at();

// Delete element at at the tail of the list
void test_delete_tail();

// Delete element at a specific index of the list
void test_delete_at();

// Returns the element of the list at the specified position
void test_get_elem_at();

// Returns the number of elements currently stored in the list
void test_list_size();

// Returns the elemt pointed by the iterator current index
void test_iterator_get_elem_at();

// Check if the iterator is still valid
void test_iterator_valid();

//print the number of test success and test failure
void print_test_exit();