#include "list.h"
#include "test_list.h"
#include <stdlib.h>
#include <stdio.h>
#include <stddef.h>

void merge(List* list1, List* list2, int ordinamento){
    int l1=list_size(list1);
    int l2=list_size(list2);
    int idx1=0, idx2=0;

    List* output_list= new_empty_list(l1+l2);

    if(ordinamento==0){ //numerico crescente
        while(idx1<l1 || idx2<l2){
        	if(idx1==l1){
        		for(idx2; idx2<l2; idx2++){
        			insert_tail(output_list, (void*)get_elem_at(list2,idx2));
        		}
        	}else if(idx2==l2){
        		for(idx1; idx1<l1; idx1++){
        			insert_tail(output_list, (void*)get_elem_at(list1,idx1));
        		}
        	}else{
		        if((void*)get_elem_at(list1,idx1) <= (void*)get_elem_at(list2,idx2)){
		            insert_tail(output_list, (void*)get_elem_at(list1,idx1));
		            idx1++;
		        }else{
		            insert_tail(output_list, (void*)get_elem_at(list2,idx2));
		            idx2++;
		        }
            }
        }
    }else if(ordinamento==1){ //numerico decrescente
        while(idx1<l1 || idx2<l2){
        	if(idx1==l1){
        		for(idx2; idx2<l2; idx2++){
        			insert_tail(output_list, (void*)get_elem_at(list2,idx2));
        		}
        	}else if(idx2==l2){
        		for(idx1; idx1<l1; idx1++){
        			insert_tail(output_list, (void*)get_elem_at(list1,idx1));
        		}
        	}else{
		        if((void*)get_elem_at(list1,idx1) >= (void*)get_elem_at(list2,idx2)){
		            insert_tail(output_list, (void*)get_elem_at(list1,idx1));
		            idx1++;
		        }else{
		            insert_tail(output_list, (void*)get_elem_at(list2,idx2));
		            idx2++;
		        }
            }
        }
    }
    fprintf(stderr, "Listaoutput:\n");
    print_list(output_list);
}

int main(){

	fprintf(stderr, "Test: \n");
    test_list_empty();
    test_insert_tail();
    test_insert_at();
    test_delete_tail();
    test_delete_at();
    test_get_elem_at();
    test_list_size();
    test_iterator_get_elem_at();
    test_iterator_valid();

    print_test_exit();  
	fprintf(stderr, "\n\n\n");
	
	fprintf(stderr, "Caso d'uso mergesort\n\n\n");
    List* l1=new_empty_list(4);
    
    void *a=(void*)1;
    insert_tail(l1, a);
    a=(void*)3;
    insert_tail(l1, a);
    a=(void*)5;
    insert_tail(l1, a);
    a=(void*)7;
    insert_tail(l1, a);

    fprintf(stderr, "Lista1:\n");
    print_list(l1);

    List* l2=new_empty_list(5);
    
    a=(void*)2;
    insert_tail(l2, a);
    a=(void*)4;
    insert_tail(l2, a);
    a=(void*)6;
    insert_tail(l2, a);
    a=(void*)8;
    insert_tail(l2, a);
    a=(void*)10;
    insert_tail(l2, a);

    fprintf(stderr, "Lista2:\n");
    print_list(l2);

    merge(l1, l2, 0);

    
    List* l3=new_empty_list(4);
    
    a=(void*)7;
    insert_tail(l3, a);
    a=(void*)5;
    insert_tail(l3, a);
    a=(void*)3;
    insert_tail(l3, a);
    a=(void*)1;
    insert_tail(l3, a);

    fprintf(stderr, "Lista1:\n");
    print_list(l3);

    List* l4=new_empty_list(5);
    
    a=(void*)10;
    insert_tail(l4, a);
    a=(void*)8;
    insert_tail(l4, a);
    a=(void*)6;
    insert_tail(l4, a);
    a=(void*)4;
    insert_tail(l4, a);
    a=(void*)2;
    insert_tail(l4, a);

    fprintf(stderr, "Lista2:\n");
    print_list(l4);
    merge(l3, l4, 1);
}    

