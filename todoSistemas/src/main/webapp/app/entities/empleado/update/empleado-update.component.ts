import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEmpleado, Empleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { TipoIdentificacion } from 'app/entities/enumerations/tipo-identificacion.model';
import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.component.html',
})
export class EmpleadoUpdateComponent implements OnInit {
  isSaving = false;
  tipoIdentificacionValues = Object.keys(TipoIdentificacion);

  editForm = this.fb.group({
    id: [],
    nombres: [],
    apellidos: [],
    fechaNacimiento: [],
    tipoIdentificacion: [],
    numeroIdentificacion: [],
  });

  constructor(protected empleadoService: EmpleadoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      if (empleado.id === undefined) {
        const today = dayjs().startOf('day');
        empleado.fechaEstimadaEjecucion = today;
      }

      this.updateForm(empleado);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleado = this.createFromForm();
    if (empleado.id !== undefined) {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleado>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(empleado: IEmpleado): void {
    this.editForm.patchValue({
      id: empleado.id,
      nombres: empleado.nombres,
      apellidos: empleado.apellidos,
      fechaNacimiento: empleado.fechaNacimiento ? empleado.fechaNacimiento.format(DATE_TIME_FORMAT) : null,
      tipoIdentificacion: empleado.tipoIdentificacion,
      numeroIdentificacion: empleado.numeroIdentificacion,
    });
  }

  protected createFromForm(): IEmpleado {
    return {
      ...new Empleado(),
      id: this.editForm.get(['id'])!.value,
      nombres: this.editForm.get(['nombres'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value
        ? dayjs(this.editForm.get(['fechaNacimiento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tipoIdentificacion: this.editForm.get(['tipoIdentificacion'])!.value,
      numeroIdentificacion: this.editForm.get(['numeroIdentificacion'])!.value,
    };
  }
}
