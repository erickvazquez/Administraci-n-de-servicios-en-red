// Compilar gcc -o hilos hilos.c –lpthread

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <math.h>
#define NUM_HILOS 1 


struct arrays{
	int * array;
};

struct envio{

    int tamanio;
    int * id;
};
int I = 0;
int indice = 0;
int * tamanioArreglo;
int comp (const void * elem1, const void * elem2) 
{
    int f = *((int*)elem1);
    int s = *((int*)elem2);
    if (f > s) return  1;
    if (f < s) return -1;
    return 0;
}

void *codigo_del_hilo(struct envio *miid){
    indice++;
     printf("canasta %d:\n", indice );
    for(int i = 0; i < miid->tamanio; i++){
        printf("%d ", *(miid->id + i));
    }
	qsort(miid->id, miid->tamanio, sizeof(int), comp);
    //printf("\n\nEl tamanio del arreglo a ordenar es: %d\n", miid->tamanio);
    printf("\n\n\n");
        pthread_exit(miid->id);
}

int main(int argc , char *argv []) {
    int h, numeroCanastas;
	printf("Ingrese numeor de canastas ");
	scanf("%d", &numeroCanastas);
    pthread_t hilos[numeroCanastas];
    int id[3500];
    int error;
    int *salida;
	int numero;
	int indices[numeroCanastas];
    int rangos[numeroCanastas];
    struct envio misEnvios[numeroCanastas];
	struct arrays canastas[numeroCanastas];
    srand(time(NULL));
    for(int i = 0; i < numeroCanastas; i++)
        indices[i] = 0;
	for(int i = 0; i < 3500; i++){
		numero = rand () % (999-0+1) + 0;
		id[i] = numero;	
	}
    for(int i = 0; i < numeroCanastas; i++)
        canastas[i].array = (int *)malloc(sizeof(int) * 3500);
    
	for(int i = 0; i < numeroCanastas; i++)
        rangos[i] = (((int)ceil((double)(999/(double)numeroCanastas)))*(i + 1));

    for(int i = 0; i < 3500; i++){
        for(int j = 0; j < numeroCanastas; j++){
            if(j == 0){
                if(id[i] <= rangos[j]){
                    *(canastas[j].array + indices[j]) = id[i];
                    indices[j]++;
                }
            }
            else {
                if(id[i] > rangos[j-1] && id[i] <= rangos[j]){
                    *(canastas[j].array + indices[j]) = id[i];
                    indices[j]++;   
                } 
             }      
        }
    }


    tamanioArreglo = (int *)malloc(numeroCanastas * sizeof(int));
    for(int i = 0; i < numeroCanastas; i++)
        *(tamanioArreglo + i) = indices[i];

   /* for(int i = 0; i < numeroCanastas; i++){
            printf("numero del indice %d: %d\n", i+1, indices[i]);
    }*/

    for(int i = 0; i < numeroCanastas; i++)
        canastas[i].array = realloc(canastas[i].array, indices[i]*sizeof(int));

  /*  for(int i = 0; i < numeroCanastas; i++){
        for(int j = 0; j < *(tamanioArreglo + i); j++)
            printf("%d ", *(canastas[i].array + j));
        printf("\n\n");
    }*/

    for(h = 0; h < numeroCanastas; h++){
        misEnvios[h].tamanio = indices[h];
        misEnvios[h].id = canastas[h].array;
        error = pthread_create(&hilos[h], NULL, (void*)codigo_del_hilo,(void*)&misEnvios[h]);
        if(error){
          fprintf(stderr,"Error %d: %s\n", error, strerror (error));
          exit (-1);
        }
    }
    for(h = 0; h < numeroCanastas; h++){
         error = pthread_join(hilos[h], (void **)&salida);
         if(error){
           fprintf(stderr,"Error %d: %s\n", error, strerror(error));
        }
         else
          for(int j = 0; j < indices[h]; j++)
    	        printf("%d  ", *(salida + j));
        }
}
