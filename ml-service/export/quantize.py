"""
Script Quantize mô hình ONNX sang INT8/INT4 nâng cao bằng ONNX Runtime Quantizer.
"""

import os
from onnxruntime.quantization import quantize_dynamic, QuantType

INPUT_MODEL = os.path.join(os.path.dirname(__file__), "../export/qwen2.5-0.5b-int4-onnx/model.onnx")
OUTPUT_MODEL = os.path.join(os.path.dirname(__file__), "../export/qwen2.5-0.5b-int8-dynamic.onnx")


def run_quantization():
    if not os.path.exists(INPUT_MODEL):
        print(f"Bỏ qua: Không tìm thấy file mô hình ONNX tại {INPUT_MODEL}")
        return

    print(f"=== Đang thực hiện Dynamic Quantization INT8: {INPUT_MODEL} -> {OUTPUT_MODEL} ===")
    quantize_dynamic(
        model_input=INPUT_MODEL,
        model_output=OUTPUT_MODEL,
        weight_type=QuantType.QUInt8
    )
    print("=== QUANTIZATION HOÀN TẤT ===")


if __name__ == "__main__":
    run_quantization()
