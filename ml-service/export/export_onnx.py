"""
Script export mô hình Qwen 2.5 0.5B sang định dạng ONNX INT4 cho Android On-device Inference.
Sử dụng `onnxruntime-genai` model builder.
"""

import os
import sys
import subprocess

MODEL_INPUT = os.path.join(os.path.dirname(__file__), "../models/qwen2.5-0.5b-fine-tuned")
FALLBACK_MODEL = "Qwen/Qwen2.5-0.5B-Instruct"
OUTPUT_DIR = os.path.join(os.path.dirname(__file__), "../export/qwen2.5-0.5b-int4-onnx")


def export_to_onnx():
    model_path = MODEL_INPUT if os.path.exists(MODEL_INPUT) else FALLBACK_MODEL
    print(f"=== Đang Export mô hình từ [{model_path}] sang ONNX INT4 ===")
    print(f"=== Thư mục đầu ra: {OUTPUT_DIR} ===")

    cmd = [
        sys.executable, "-m", "onnxruntime_genai.models.builder",
        "-m", model_path,
        "-o", OUTPUT_DIR,
        "-p", "int4",
        "-e", "cpu"
    ]

    try:
        subprocess.run(cmd, check=True)
        print("=== EXPORT ONNX INT4 THÀNH CÔNG! ===")
        print(f"Các file thu được trong {OUTPUT_DIR}:")
        for f in os.listdir(OUTPUT_DIR):
            size_mb = os.path.getsize(os.path.join(OUTPUT_DIR, f)) / (1024 * 1024)
            print(f" - {f}: {size_mb:.2f} MB")
    except Exception as e:
        print(f"Lỗi khi export ONNX: {e}")


if __name__ == "__main__":
    export_to_onnx()
