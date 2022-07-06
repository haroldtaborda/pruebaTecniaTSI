import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IActividad, Actividad } from '../actividad.model';
import { ActividadService } from '../service/actividad.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { EstadoActividad } from 'app/entities/enumerations/estado-actividad.model';

@Component({
  selector: 'jhi-actividad-update',
  templateUrl: './actividad-update.component.html',
})
export class ActividadUpdateComponent implements OnInit {
  isSaving = false;
  estadoActividadValues = Object.keys(EstadoActividad);
  titulo = 'Actualizar Actividad';

  empleadosSharedCollection: IEmpleado[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    fechaEstimadaEjecucion: [],
    descripcion: [],
    estado: [],
    empleado: [],
  });

  constructor(
    protected actividadService: ActividadService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ actividad }) => {
      if (actividad.id === undefined) {
        const today = dayjs().startOf('day');
        actividad.fechaEstimadaEjecucion = today;
        actividad.estado = EstadoActividad.PENDIENTE;
        this.titulo = 'Crear Actividad';
      }

      this.updateForm(actividad);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const actividad = this.createFromForm();
    if (actividad.id !== undefined) {
      this.subscribeToSaveResponse(this.actividadService.update(actividad));
    } else {
      this.subscribeToSaveResponse(this.actividadService.create(actividad));
    }
  }

  trackEmpleadoById(_index: number, item: IEmpleado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActividad>>): void {
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

  protected updateForm(actividad: IActividad): void {
    this.editForm.patchValue({
      id: actividad.id,
      nombre: actividad.nombre,
      fechaEstimadaEjecucion: actividad.fechaEstimadaEjecucion ? actividad.fechaEstimadaEjecucion.format(DATE_TIME_FORMAT) : null,
      descripcion: actividad.descripcion,
      estado: actividad.estado,
      empleado: actividad.empleado,
    });

    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(
      this.empleadosSharedCollection,
      actividad.empleado
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empleadoService
      .empleadosDisnponibles()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing(empleados, this.editForm.get('empleado')!.value)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }

  protected createFromForm(): IActividad {
    return {
      ...new Actividad(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      fechaEstimadaEjecucion: this.editForm.get(['fechaEstimadaEjecucion'])!.value
        ? dayjs(this.editForm.get(['fechaEstimadaEjecucion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      descripcion: this.editForm.get(['descripcion'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      empleado: this.editForm.get(['empleado'])!.value,
    };
  }
}
