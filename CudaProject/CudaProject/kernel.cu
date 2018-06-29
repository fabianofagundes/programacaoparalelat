
#include "cuda_runtime.h"
#include "device_launch_parameters.h"

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "CImg.h"

using namespace cimg_library;

__global__ void gpu_taskR(unsigned char * d_in, unsigned char * d_out, int img_width, int img_height) {

	unsigned long scale = img_height * img_width;
	unsigned long r = blockIdx.x * blockDim.x + threadIdx.x;
	unsigned long g = r + scale;
	unsigned long b = g + scale;



	d_out[r] = d_in[r];
	d_out[g] = 0;
	d_out[b] = 0;

}

__global__ void gpu_taskG(unsigned char * d_in, unsigned char * d_out1, int img_width, int img_height) {

	unsigned long scale = img_height * img_width;
	unsigned long r = blockIdx.x * blockDim.x + threadIdx.x;
	unsigned long g = r + scale;
	unsigned long b = g + scale;

	d_out1[r] = 0;
	d_out1[g] = d_in[g];
	d_out1[b] = 0;

}

__global__ void gpu_taskB(unsigned char * d_in, unsigned char * d_out2, int img_width, int img_height) {

	unsigned long scale = img_height * img_width;
	unsigned long r = blockIdx.x * blockDim.x + threadIdx.x;
	unsigned long g = r + scale;
	unsigned long b = g + scale;

	d_out2[r] = d_in[r];
	d_out2[g] = d_in[g];
	d_out2[b] = d_in[b];

}

int main(int argc, char ** argv) {

	clock_t tinicio, tfim, tdecorrido;

	tinicio = clock();

	CImg<unsigned char> img("imagem2.bmp");
	unsigned long img_size = img.size();
	int img_width = img.width();
	int img_height = img.height();
	int img_depth = img.depth();
	int img_dim = img.spectrum();
	unsigned char *h_in = img.data();

	CImg<unsigned char> img_out(img_width, img_height, img_depth, img_dim);
	CImg<unsigned char> img_out1(img_width, img_height, img_depth, img_dim);
	CImg<unsigned char> img_out2(img_width, img_height, img_depth, img_dim);
	unsigned char *h_out = img_out.data();

	//declara ponteiros para memória da gpu
	unsigned char * d_in;
	unsigned char * d_out;
	
	//aloca memória na gpu
	cudaMalloc((void**)&d_in, img_size);
	cudaMalloc((void**)&d_out, img_size);

	//transfere dados para a gpu
	cudaMemcpy(d_in, h_in, img_size, cudaMemcpyHostToDevice);

	dim3 grid(512);         // 512 x 1 x 1
	dim3 block(1024); // 1024 x 1024 x 1

	//executa comando kernel
	gpu_taskR <<<grid, block >>> (d_in, d_out, img_width, img_height);

	//espera a execução da gpu
	cudaDeviceSynchronize();

	//transfere dados para a cpu devolta
	cudaMemcpy(d_in, h_in, img_size, cudaMemcpyDeviceToHost);

	gpu_taskG << <grid, block >> > (d_in, d_out, img_width, img_height);
	cudaDeviceSynchronize();

	gpu_taskB << <grid, block >> > (d_in, d_out, img_width, img_height);
	cudaDeviceSynchronize();


	for (int i=0; i < img_width; i++) {
		for (int j = 0; j < img_height; j++) {
			img_out(i, j, 0) = img(i, j, 0);
			img_out(i, j, 1) = 0;
			img_out(i, j, 2) = 0;
		}	
	}

	for (int i = 0; i < img_width; i++) {
		for (int j = 0; j < img_height; j++) {
			img_out1(i, j, 1) = img(i, j, 0);
			img_out1(i, j, 2) = 0;
			img_out1(i, j, 0) = 0;
		}
	}

	for (int i = 0; i < img_width; i++) {
		for (int j = 0; j < img_height; j++) {
			img_out2(i, j, 2) = img(i, j, 0);
			img_out2(i, j, 1) = 0;
			img_out2(i, j, 0) = 0;
		}
	}
	
	//CImgDisplay main_disp(img_out, "Após Processar RED");
	//CImgDisplay main_disp1(img_out1, "Após Processar GREEN");
	//CImgDisplay main_disp2(img_out2, "Após Processar BLUE");
	
	img_out.save("RED.bmp", -1, 3);
	img_out1.save("GREEN.bmp", -1, 5);
	img_out2.save("BLUE.bmp", -1, 4);
	/*
	while (!main_disp.is_closed()) {
		main_disp.wait();
	}

	while (!main_disp1.is_closed()) {
		main_disp.wait();
	}

	while (!main_disp2.is_closed()) {
		main_disp.wait();
	}
	*/

	tfim = clock();
	tdecorrido = ((tfim - tinicio) / (CLOCKS_PER_SEC / 1000));

	cudaFree(d_in);
	cudaFree(d_out);

	printf("TEMPO: %d milseg \n", tdecorrido);

	return 0;
}



