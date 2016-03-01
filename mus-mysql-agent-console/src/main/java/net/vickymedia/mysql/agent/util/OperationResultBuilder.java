package net.vickymedia.mysql.agent.util;

import net.vickymedia.mus.dto.OperationResult;

import java.util.Map;

/**
 * User: weijie.song
 * Date: 16/1/22 下午4:38
 */
public class OperationResultBuilder<T> {
	private boolean exists = Boolean.FALSE;
	private boolean success = Boolean.FALSE;
	private Map<String, String> extProps;
	private T data;

	public static OperationResultBuilder create() {
		return new OperationResultBuilder();
	}

	public static OperationResultBuilder setData() {
		return new OperationResultBuilder();
	}

	public OperationResultBuilder setSuccess(boolean result) {
		this.success = result;
		return this;
	}

	public OperationResultBuilder setData(T data) {
		this.data = data;
		return this;
	}

	public OperationResultBuilder setExists(boolean exists) {
		this.exists = exists;
		return this;
	}

	public OperationResult build() {
		OperationResult operationResult = new OperationResult();
		operationResult.setData(this.data);
		operationResult.setExists(this.exists);
		operationResult.setSuccess(this.success);
		return operationResult;
	}

	public static <T> OperationResult success(T data) {
		return create().setData(data).setExists(data != null).setSuccess(Boolean.TRUE).build();
	}

	public static OperationResult success() {
		return create().setSuccess(Boolean.TRUE).build();
	}

	public static OperationResult failed() {
		return create().build();
	}
}
