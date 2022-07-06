import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IActividad } from '../actividad.model';
import { ActividadService } from '../service/actividad.service';
import { ActividadDeleteDialogComponent } from '../delete/actividad-delete-dialog.component';
import { EstadoActividad } from 'app/entities/enumerations/estado-actividad.model';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-actividad',
  templateUrl: './actividad.component.html',
})
export class ActividadComponent implements OnInit {
  // SE CREA EL VIEWCHILD PARA GENERAR EL MODAL QUE VALIDA LA FINALIZACION DE LA ACTIVIDAD.
  @ViewChild('validarFinalizar', { static: true }) content: ElementRef | undefined;

  actividads?: IActividad[];
  isLoading = false;
  actividad?: IActividad | null;

  constructor(protected actividadService: ActividadService, protected modalService: NgbModal, protected alertService: AlertService) {}

  loadAll(): void {
    this.isLoading = true;

    this.actividadService.query().subscribe({
      next: (res: HttpResponse<IActividad[]>) => {
        this.isLoading = false;
        this.actividads = res.body ?? [];
        this.actividads.forEach(element => {
          this.actividadService.getDelayDays(element.id!).subscribe((resp: HttpResponse<string>) => {
            element.diasRetraso = resp.body;
          });
        });
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  openModalFinalize(actividad: IActividad): void {
    this.modalService.open(this.content);
    this.actividad = actividad;
  }

  cancel(): void {
    this.modalService.dismissAll();
  }

  completeFinalize(): void {
    if (this.actividad) {
      this.actividad.estado = EstadoActividad.REALIZADA;
      this.actividadService.update(this.actividad).subscribe({
        next: () => {
          this.alertService.addAlert({
            type: 'success',
            message: 'Actividad finalizada con exito.',
          });
          window.location.reload();
        },
        error: () => {
          this.alertService.addAlert({
            type: 'danger',
            message: 'El error no se puedo finalizar correctamente, vuelve a intentarlo.',
          });
        },
      });
    }
  }

  trackId(_index: number, item: IActividad): number {
    return item.id!;
  }

  delete(actividad: IActividad): void {
    const modalRef = this.modalService.open(ActividadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.actividad = actividad;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
